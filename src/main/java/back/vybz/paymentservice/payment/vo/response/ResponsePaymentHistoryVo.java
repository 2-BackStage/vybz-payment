package back.vybz.paymentservice.payment.vo.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponsePaymentHistoryVo {

    private Integer amount;

    private Integer ticketCount;

    private String approvedAt;

    @Builder
    public ResponsePaymentHistoryVo(Integer amount, Integer ticketCount,
                                    String approvedAt) {
        this.amount = amount;
        this.ticketCount = ticketCount;
        this.approvedAt = approvedAt;
    }
}
