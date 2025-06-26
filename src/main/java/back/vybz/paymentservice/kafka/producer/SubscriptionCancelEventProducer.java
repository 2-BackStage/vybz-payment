package back.vybz.paymentservice.kafka.producer;

import back.vybz.paymentservice.kafka.event.PaymentRefundEvent;
import back.vybz.paymentservice.kafka.event.SubscriptionCancelEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionCancelEventProducer {

    private final KafkaTemplate<String, SubscriptionCancelEvent> subscriptionCancelEventKafkaTemplate;

    private final String CREATE_USER_TOPIC = "cancel-subscription";

    public void setSubscriptionCancelEventKafkaTemplate(SubscriptionCancelEvent subscriptionCancelEvent) {
        log.info("[Kafka] Sending SubscriptionCancelEvent to topic '{}': {}", CREATE_USER_TOPIC, subscriptionCancelEvent);

        try {
            CompletableFuture<SendResult<String, SubscriptionCancelEvent>> future = subscriptionCancelEventKafkaTemplate.send(CREATE_USER_TOPIC, subscriptionCancelEvent);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("[Kafka] Failed to send SubscriptionCancelEvent: {}", ex.getMessage(), ex);

                } else {
                    log.info("[Kafka] Successfully sent SubscriptionCancelEvent. Topic: {}, Partition: {}, Offset: {}",
                            result.getRecordMetadata().topic(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                }
            });
        } catch (Exception e) {
            log.error("[Kafka] Failed to send SubscriptionCancelEvent: {}", e.getMessage(), e);
        }
    }
}
