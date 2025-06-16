package back.vybz.paymentservice.kafka.producer;

import back.vybz.paymentservice.kafka.event.PaymentConfirmEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentConfirmProducer {

    private final KafkaTemplate<String, PaymentConfirmEvent> kafkaTemplate;

    public static final String CREATE_USER_TOPIC = "create-payment-confirm";

    public void sendPaymentConfirmEvent(PaymentConfirmEvent paymentConfirmEvent) {
        log.info("[Kafka] Sending PaymentConfirmEvent to topic '{}': {}", CREATE_USER_TOPIC, paymentConfirmEvent);

        try {
            CompletableFuture<SendResult<String, PaymentConfirmEvent>> future = kafkaTemplate.send(CREATE_USER_TOPIC, paymentConfirmEvent);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("[Kafka] Failed to send PaymentConfirmEvent: {}", ex.getMessage(), ex);

                } else {
                    log.info("[Kafka] Successfully sent PaymentConfirmEvent. Topic: {}, Partition: {}, Offset: {}",
                            result.getRecordMetadata().topic(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                }
            });
        } catch (Exception e) {
            log.error("[Kafka] Failed to send PaymentConfirmEvent: {}", e.getMessage(), e);
        }
    }
}
