package back.vybz.paymentservice.subscription.batch.writer;

import back.vybz.paymentservice.kafka.event.SubscriptionEvent;
import back.vybz.paymentservice.kafka.producer.SubscriptionEventProducer;
import back.vybz.paymentservice.subscription.application.SubscriptionService;
import back.vybz.paymentservice.subscription.batch.dto.BillingDto;
import back.vybz.paymentservice.subscription.batch.policy.BillingPaymentPolicy;
import back.vybz.paymentservice.subscription.dto.request.RequestSubscriptionExecuteDto;
import back.vybz.paymentservice.subscription.infrastructure.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class BillingPaymentWriter implements ItemWriter<BillingDto> {

    private final SubscriptionService subscriptionService;

    private final SubscriptionRepository subscriptionRepository;

    private final SubscriptionEventProducer subscriptionEventProducer;

    @Override
    public void write(Chunk<? extends BillingDto> items) {
        for (BillingDto billingDto : items) {
            try {

                RequestSubscriptionExecuteDto requestSubscriptionExecuteDto = RequestSubscriptionExecuteDto.builder()
                        .customerKey(billingDto.getCustomerKey())
                        .billingKey(billingDto.getTossBillingKey())
                        .price(billingDto.getPrice())
                        .orderName("정기 후원 결제")
                        .build();

                subscriptionService.executeBillingPayment(requestSubscriptionExecuteDto);
                log.info("✅ 정기결제 성공 - user: {}, busker: {}", billingDto.getUserUuid(), billingDto.getBuskerUuid());

                subscriptionRepository.findByUserUuidAndBuskerUuid(
                        billingDto.getUserUuid(), billingDto.getBuskerUuid()
                ).ifPresent(subscription -> {
                    subscription.resetFailCount();
                    subscription.updateNextPaymentAt(BillingPaymentPolicy.calculateNextPaymentAt(LocalDateTime.now()));

                    subscriptionRepository.save(subscription);
                    log.info("📅 다음 결제일 갱신 - user: {}, nextPaymentAt: {}", subscription.getUserUuid(), subscription.getNextPaymentAt());
                });

                subscriptionEventProducer.sendSubscriptionEvent(SubscriptionEvent.builder()
                        .userUuid(billingDto.getUserUuid())
                        .buskerUuid(billingDto.getBuskerUuid())
                        .price(billingDto.getPrice())
                        .build());

            } catch (Exception e) {
                log.warn("❌ 정기결제 실패 - user: {}, 이유: {}", billingDto.getUserUuid(), e.getMessage());

                subscriptionRepository.findByUserUuidAndBuskerUuid(
                        billingDto.getUserUuid(), billingDto.getBuskerUuid()
                ).ifPresent(subscription -> {
                    subscription.increaseFailCount();

                    subscriptionRepository.save(subscription);
                    log.info("🔁 실패 횟수 증가 - user: {}, failCount: {}, nextPaymentAt: {}", subscription.getUserUuid(), subscription.getFailCount(), subscription.getNextPaymentAt());
                });
            }
        }
    }
}
