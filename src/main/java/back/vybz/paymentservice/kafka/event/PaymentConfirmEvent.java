package back.vybz.paymentservice.kafka.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentConfirmEvent {

    private String userUuid;

    private Integer ticketCount;

    @Builder
    public PaymentConfirmEvent(String userUuid, Integer ticketCount) {
        this.userUuid = userUuid;
        this.ticketCount = ticketCount;
    }
}
