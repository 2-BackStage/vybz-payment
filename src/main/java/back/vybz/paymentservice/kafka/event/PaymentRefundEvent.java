package back.vybz.paymentservice.kafka.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentRefundEvent {

    private String userUuid;

    private Integer ticketCount;

    private Integer amount;

    @Builder
    public PaymentRefundEvent(String userUuid, Integer ticketCount, Integer amount) {
        this.userUuid = userUuid;
        this.ticketCount = ticketCount;
        this.amount = amount;
    }
}
