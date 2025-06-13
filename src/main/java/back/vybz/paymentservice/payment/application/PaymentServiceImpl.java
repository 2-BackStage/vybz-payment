package back.vybz.paymentservice.payment.application;

import back.vybz.paymentservice.common.entity.BaseResponseStatus;
import back.vybz.paymentservice.common.exception.BaseException;
import back.vybz.paymentservice.payment.domain.Payment;
import back.vybz.paymentservice.payment.domain.PaymentStatus;
import back.vybz.paymentservice.payment.domain.RefundHistory;
import back.vybz.paymentservice.payment.domain.RefundStatus;
import back.vybz.paymentservice.payment.dto.request.RequestPaymentCancelDto;
import back.vybz.paymentservice.payment.dto.request.RequestPaymentConfirmDto;
import back.vybz.paymentservice.payment.dto.request.RequestPaymentCreateDto;
import back.vybz.paymentservice.payment.dto.request.RequestPaymentFailDto;
import back.vybz.paymentservice.payment.dto.response.ResponsePaymentConfirmDto;
import back.vybz.paymentservice.payment.dto.response.ResponsePaymentCreateDto;
import back.vybz.paymentservice.payment.infrastructure.PaymentRepository;
import back.vybz.paymentservice.payment.infrastructure.RefundHistoryRepository;
import back.vybz.paymentservice.payment.util.TossHeaderHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final RefundHistoryRepository refundHistoryRepository;

    private final TossHeaderHelper tossHeaderHelper;

    private final RestTemplate restTemplate;

    private static final int TICKET_UNIT_PRICE = 110;

    @Value("${payment.base-url}")
    private String baseUrl;

    @Value("${payment.success-url}")
    private String successUrl;

    @Value("${payment.fail-url}")
    private String failUrl;

    // 결제 생성
    @Override
    public ResponsePaymentCreateDto addPayment(RequestPaymentCreateDto requestPaymentCreateDto) {

        Payment payment = requestPaymentCreateDto.toEntity();
        payment.markRequestedAt(LocalDateTime.now());

        paymentRepository.save(payment);

        HttpHeaders headers = tossHeaderHelper.createAuthHeaders();

        Map<String, Object> body = new HashMap<>();

        body.put("method", payment.getMethod());
        body.put("orderId", payment.getOrderId());
        body.put("amount", payment.getAmount());
        body.put("orderName", payment.getOrderName());
        body.put("successUrl", successUrl);
        body.put("failUrl", failUrl);

        body.put("validHours", 1);

        HttpEntity<Map<String, Object>> httpRequest = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl, httpRequest, Map.class);

        Map responseBody = response.getBody();


        Object checkoutObj = responseBody.get("checkout");

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> checkout = objectMapper.convertValue(checkoutObj, new TypeReference<>() {});

        if (checkout == null || !checkout.containsKey("url")) {
            throw new BaseException(BaseResponseStatus.TOSS_EMPTY_RESPONSE);
        }
        String checkoutUrl = (String) checkout.get("url");

        return ResponsePaymentCreateDto.builder()
                .checkoutUrl(checkoutUrl)
                .orderId(payment.getOrderId())
                .build();
    }

    // 결제 승인
    @Transactional
    @Override
    public ResponsePaymentConfirmDto confirmPayment(RequestPaymentConfirmDto requestPaymentConfirmDto) {

        HttpHeaders headers = tossHeaderHelper.createAuthHeaders();

        Map<String, Object> body = new HashMap<>();

        body.put("paymentKey", requestPaymentConfirmDto.getPaymentKey());
        body.put("orderId", requestPaymentConfirmDto.getOrderId());
        body.put("amount", requestPaymentConfirmDto.getAmount());

        HttpEntity<Map<String, Object>> httpRequest = new HttpEntity<>(body, headers);

        Payment payment = paymentRepository
                .findByOrderId(requestPaymentConfirmDto.getOrderId())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PAYMENT_NO_EXIST));

        if (payment.getPaymentStatus() != PaymentStatus.READY) {
            throw new BaseException(BaseResponseStatus.PAYMENT_ALREADY_DONE);
        }

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    baseUrl + "/confirm", httpRequest, Map.class
            );

            Map responseBody = response.getBody();

            if (responseBody == null) {
                throw new BaseException(BaseResponseStatus.TOSS_EMPTY_RESPONSE);
            }

            String paymentKey = (String) responseBody.get("paymentKey");
            String orderId = (String) responseBody.get("orderId");
            String method = (String) responseBody.get("method");
            Integer amount = (Integer) responseBody.get("totalAmount");
            PaymentStatus paymentStatus = PaymentStatus.valueOf((String) responseBody.get("status"));

            Map<String, Object> failure = (Map<String, Object>) responseBody.get("failure");

            LocalDateTime approvedAt = Optional.ofNullable((String) responseBody.get("approvedAt"))
                    .map(OffsetDateTime::parse)
                    .map(OffsetDateTime::toLocalDateTime)
                    .orElse(null);

            if (failure != null) {

                String failCode = (String) failure.get("code");
                String failReason = (String) failure.get("message");

                RequestPaymentFailDto requestPaymentFailDto = RequestPaymentFailDto.builder()
                        .orderId(orderId)
                        .failCode(failCode)
                        .failReason(failReason)
                        .build();

                markAsFailedPayment(requestPaymentFailDto);

                throw new BaseException(BaseResponseStatus.TOSS_PAYMENT_REJECTED);
            }

            if (!Objects.equals(amount, payment.getAmount())) {
                throw new BaseException(BaseResponseStatus.PAYMENT_AMOUNT_MISMATCH);
            }

            payment.approve(paymentKey, method, approvedAt);

            int ticketCount = payment.getAmount() / TICKET_UNIT_PRICE;

            System.out.println("📂 ticketCount 확인:  " + ticketCount);

            // TODO: kafka producer 처리 예정

            return ResponsePaymentConfirmDto.builder()
                    .paymentKey(paymentKey)
                    .message(paymentStatus.getDescription())
                    .orderId(orderId)
                    .paymentStatus(paymentStatus)
                    .approvedAt(approvedAt != null ? approvedAt.toString() : null)
                    .method(method)
                    .orderName(payment.getOrderName())
                    .amount(payment.getAmount())
                    .build();

        } catch (HttpClientErrorException e) {

            String errorBody = e.getResponseBodyAsString();
            log.error("[Toss 결제 오류] {}", errorBody);

            throw new BaseException(BaseResponseStatus.TOSS_API_CALL_FAIL);
        }
    }

    // 결제 취소
    @Transactional
    @Override
    public void cancelPayment(String paymentKey,RequestPaymentCancelDto requestPaymentCancelDto) {

        Payment payment = paymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PAYMENT_NOT_FOUND));

        if (payment.getPaymentStatus() == PaymentStatus.CANCELED) {
            throw new BaseException(BaseResponseStatus.PAYMENT_ALREADY_CANCELED);
        }

        if (payment.getPaymentStatus() != PaymentStatus.DONE) {
            throw new BaseException(BaseResponseStatus.PAYMENT_NOT_DONE);
        }

        if(refundHistoryRepository.existsByPaymentKey(paymentKey)) {
            throw new BaseException(BaseResponseStatus.PAYMENT_CANCEL_ALREADY_REQUESTED);
        }

        HttpHeaders headers = tossHeaderHelper.createAuthHeaders();

        Map<String, Object> body = new HashMap<>();

        body.put("cancelReason", requestPaymentCancelDto.getCancelReason());

        HttpEntity<Map<String, Object>> httpRequest = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl + "/{paymentKey}/cancel", httpRequest, Map.class, paymentKey
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            Map responseBody = response.getBody();

            List<Map<String, Object>> cancels = (List<Map<String, Object>>) responseBody.get("cancels");

            Map<String, Object> cancelData = cancels != null && !cancels.isEmpty() ? cancels.get(0) : null;

            Integer refundAmount = cancelData != null && cancelData.get("cancelAmount") != null
                    ? ((Number) cancelData.get("cancelAmount")).intValue()
                    : 0;

            payment.cancel();

            RefundHistory refundHistory = RefundHistory.builder()
                    .userUuid(payment.getUserUuid())
                    .paymentKey(payment.getPaymentKey())
                    .orderId(payment.getOrderId())
                    .refundAmount(refundAmount)
                    .cancelReason(requestPaymentCancelDto.getCancelReason())
                    .refundStatus(RefundStatus.COMPLETED)
                    .refundAt(LocalDateTime.now())
                    .completedAt(LocalDateTime.now())
                    .build();

            refundHistoryRepository.save(refundHistory);
        } else {
            throw new BaseException(BaseResponseStatus.TOSS_API_CALL_FAIL);
        }
    }

    @Transactional
    @Override
    public void markAsFailedPayment(RequestPaymentFailDto requestPaymentFailDto) {
        Payment payment = paymentRepository.findByOrderId(requestPaymentFailDto.getOrderId())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PAYMENT_NO_EXIST));

        payment.failPayment(requestPaymentFailDto.getFailCode(), requestPaymentFailDto.getFailReason());
    }
}
