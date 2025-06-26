package back.vybz.paymentservice.kafka.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SubscriptionEvent {

    private String userUuid;

    private String buskerUuid;

    private Integer price;

    @Builder
    public SubscriptionEvent(String userUuid, String buskerUuid, Integer price) {
        this.userUuid = userUuid;
        this.buskerUuid = buskerUuid;
        this.price = price;
    }
}
