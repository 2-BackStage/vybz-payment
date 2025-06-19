package back.vybz.paymentservice.kafka.producer;

import back.vybz.paymentservice.kafka.event.PaymentConfirmEvent;
import back.vybz.paymentservice.kafka.event.PaymentRefundEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentRefundProducer {

    private final KafkaTemplate<String, PaymentRefundEvent> paymentRefundKafkaTemplate;

    public static final String CREATE_USER_TOPIC = "update-payment";

    public void sendPaymentRefundEvent(PaymentRefundEvent paymentRefundEvent) {
        log.info("[Kafka] Sending PaymentRefundEvent to topic '{}': {}", CREATE_USER_TOPIC, paymentRefundEvent);

        try {
            CompletableFuture<SendResult<String, PaymentRefundEvent>> future = paymentRefundKafkaTemplate.send(CREATE_USER_TOPIC, paymentRefundEvent);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("[Kafka] Failed to send PaymentRefundEvent: {}", ex.getMessage(), ex);

                } else {
                    log.info("[Kafka] Successfully sent PaymentRefundEvent. Topic: {}, Partition: {}, Offset: {}",
                            result.getRecordMetadata().topic(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                }
            });
        } catch (Exception e) {
            log.error("[Kafka] Failed to send PaymentRefundEvent: {}", e.getMessage(), e);
        }
    }
}
