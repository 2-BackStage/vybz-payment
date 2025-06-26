package back.vybz.paymentservice.kafka.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SubscriptionCancelEvent {

    private String userUuid;

    private String buskerUuid;

    @Builder
    public SubscriptionCancelEvent(String userUuid, String buskerUuid) {
        this.userUuid = userUuid;
        this.buskerUuid = buskerUuid;
    }
}
