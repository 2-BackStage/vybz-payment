package back.vybz.paymentservice.kafka.producer;

import back.vybz.paymentservice.kafka.event.SubscriptionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionEventProducer {

    private final KafkaTemplate<String, SubscriptionEvent> subscriptionEventKafkaTemplate;

    public static final String CREATE_USER_TOPIC = "subscription-completed";

    public void sendSubscriptionEvent(SubscriptionEvent subscriptionEvent) {
        log.info("[Kafka] Sending SubscriptionEvent to topic '{}': {}", CREATE_USER_TOPIC, subscriptionEvent);

        try {
            CompletableFuture<SendResult<String, SubscriptionEvent>> future = subscriptionEventKafkaTemplate.send(CREATE_USER_TOPIC, subscriptionEvent);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("[Kafka] Failed to send SubscriptionEvent: {}", ex.getMessage(), ex);

                } else {
                    log.info("[Kafka] Successfully sent SubscriptionEvent. Topic: {}, Partition: {}, Offset: {}",
                            result.getRecordMetadata().topic(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                }
            });
        } catch (Exception e) {
            log.error("[Kafka] Failed to send SubscriptionEvent: {}", e.getMessage(), e);
        }
    }
}
