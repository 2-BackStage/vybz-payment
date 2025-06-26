package back.vybz.paymentservice.subscription.application;

import back.vybz.paymentservice.common.entity.BaseResponseStatus;
import back.vybz.paymentservice.common.exception.BaseException;
import back.vybz.paymentservice.kafka.event.SubscriptionCancelEvent;
import back.vybz.paymentservice.kafka.event.SubscriptionEvent;
import back.vybz.paymentservice.kafka.producer.SubscriptionCancelEventProducer;
import back.vybz.paymentservice.kafka.producer.SubscriptionEventProducer;
import back.vybz.paymentservice.payment.domain.*;
import back.vybz.paymentservice.subscription.dto.request.RequestSubscriptionCancelDto;
import back.vybz.paymentservice.subscription.dto.request.RequestSubscriptionCreateDto;
import back.vybz.paymentservice.subscription.dto.request.RequestSubscriptionExecuteDto;
import back.vybz.paymentservice.subscription.dto.request.RequestTossBillingKeyDto;
import back.vybz.paymentservice.subscription.dto.response.ResponseBillingKeyDto;
import back.vybz.paymentservice.subscription.infrastructure.SubscriptionRepository;
import back.vybz.paymentservice.payment.infrastructure.PaymentRepository;
import back.vybz.paymentservice.common.util.TossHeaderHelper;
import back.vybz.paymentservice.subscription.domain.Subscription;
import back.vybz.paymentservice.subscription.domain.SubscriptionStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository membershipRepository;

    private final PaymentRepository paymentRepository;

    private final TossHeaderHelper tossHeaderHelper;

    private final RestTemplate restTemplate;

    private final SubscriptionEventProducer subscriptionEventProducer;

    private final SubscriptionCancelEventProducer subscriptionCancelEventProducer;

    private final SubscriptionRepository subscriptionRepository;

    @Transactional
    @Override
    public ResponseBillingKeyDto registerBillingKey(RequestSubscriptionCreateDto requestSubscriptionCreateDto) {

        HttpHeaders headers = tossHeaderHelper.createAuthHeaders();

        RequestTossBillingKeyDto body = new RequestTossBillingKeyDto(
                requestSubscriptionCreateDto.getAuthKey(),
                requestSubscriptionCreateDto.getCustomerKey()
        );

        HttpEntity<RequestTossBillingKeyDto> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/billing/authorizations/issue",
                request,
                Map.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new BaseException(BaseResponseStatus.TOSS_PAYMENT_FAILED);
        }

        String billingKey = (String) response.getBody().get("billingKey");

        Subscription subscription = Subscription.builder()
                .userUuid(requestSubscriptionCreateDto.getUserUuid())
                .buskerUuid(requestSubscriptionCreateDto.getBuskerUuid())
                .customerKey(requestSubscriptionCreateDto.getCustomerKey())
                .tossBillingKey(billingKey)
                .price(requestSubscriptionCreateDto.getPrice())
                .failCount(0)
                .subscriptionStatus(SubscriptionStatus.READY)
                .build();

        membershipRepository.save(subscription);

        log.info("🔥 발급된 billingKey: {}", billingKey);

        return new ResponseBillingKeyDto(billingKey);
    }

    @Transactional
    @Override
    public void executeBillingPayment(RequestSubscriptionExecuteDto requestSubscriptionExecuteDto) {

        Subscription subscription = membershipRepository.findByCustomerKey(requestSubscriptionExecuteDto.getCustomerKey())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.SUBSCRIPTION_NOT_FOUND));

        String orderId = UUID.randomUUID().toString();

        HttpHeaders headers = tossHeaderHelper.createAuthHeaders();

        Map<String, Object> body = new HashMap<>();
        body.put("customerKey", requestSubscriptionExecuteDto.getCustomerKey());
        body.put("orderId", orderId);
        body.put("billingKey", requestSubscriptionExecuteDto.getBillingKey());
        body.put("amount", requestSubscriptionExecuteDto.getPrice());
        body.put("orderName", requestSubscriptionExecuteDto.getOrderName());

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        LocalDateTime now = LocalDateTime.now();

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/billing/" + subscription.getTossBillingKey(),
                request,
                Map.class
        );

        Map<String, Object> responseBody = response.getBody();

        // 결제 실패한 경우
        if (!response.getStatusCode().is2xxSuccessful()) {

            Payment failedPayment = Payment.builder()
                    .userUuid(subscription.getUserUuid())
                    .orderId(orderId)
                    .orderName(requestSubscriptionExecuteDto.getOrderName())
                    .amount(requestSubscriptionExecuteDto.getPrice())
                    .paymentStatus(PaymentStatus.ABORTED)
                    .paymentType(PaymentType.MEMBERSHIP)
                    .failCode((String) responseBody.get("failCode"))
                    .failReason((String) responseBody.get("failReason"))
                    .requestedAt(now)
                    .build();

            paymentRepository.save(failedPayment);
            throw new BaseException(BaseResponseStatus.TOSS_PAYMENT_FAILED);
        }

        // 결제 성공한 경우
        subscription.activate();
        membershipRepository.save(subscription);

        Payment successPayment = Payment.builder()
                .userUuid(subscription.getUserUuid())
                .orderId(orderId)
                .orderName(requestSubscriptionExecuteDto.getOrderName())
                .amount(requestSubscriptionExecuteDto.getPrice())
                .paymentStatus(PaymentStatus.DONE)
                .paymentType(PaymentType.MEMBERSHIP)
                .method((String) responseBody.get("method"))
                .paymentKey((String) responseBody.get("paymentKey"))
                .tossBillingKey(subscription.getTossBillingKey())
                .approvedAt(now)
                .requestedAt(now)
                .build();

        paymentRepository.save(successPayment);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                subscriptionEventProducer.sendSubscriptionEvent(SubscriptionEvent.builder()
                        .userUuid(subscription.getUserUuid())
                        .buskerUuid(subscription.getBuskerUuid())
                        .price(subscription.getPrice())
                        .build());
            }
        });
    }

    @Transactional
    @Override
    public void cancelSubscription(RequestSubscriptionCancelDto requestSubscriptionCancelDto) {

        Subscription subscription = subscriptionRepository.findByUserUuidAndBuskerUuidAndDeletedFalse(
                requestSubscriptionCancelDto.getUserUuid(), requestSubscriptionCancelDto.getBuskerUuid()
        ).orElseThrow(() -> new BaseException(BaseResponseStatus.SUBSCRIPTION_NOT_FOUND));

        if(subscription.getSubscriptionStatus() == SubscriptionStatus.CANCELED) {
            throw  new BaseException(BaseResponseStatus.ALREADY_CANCELED_SUBSCRIPTION);
        }

        subscription.cancel();

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                subscriptionCancelEventProducer.setSubscriptionCancelEventKafkaTemplate(SubscriptionCancelEvent.builder()
                        .userUuid(subscription.getUserUuid())
                        .buskerUuid(subscription.getBuskerUuid())
                        .build());
            }
        });

    }
}
