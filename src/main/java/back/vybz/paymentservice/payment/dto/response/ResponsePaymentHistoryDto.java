package back.vybz.paymentservice.payment.dto.response;

import back.vybz.paymentservice.payment.vo.response.ResponsePaymentHistoryVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponsePaymentHistoryDto {

    private Integer amount;

    private Integer ticketCount;

    private String approvedAt;

    @Builder
    public ResponsePaymentHistoryDto(Integer amount, Integer ticketCount,
                                     String approvedAt) {
        this.amount = amount;
        this.ticketCount = ticketCount;
        this.approvedAt = approvedAt;
    }

    public ResponsePaymentHistoryVo toResponsePaymentHistoryVo() {
        return ResponsePaymentHistoryVo.builder()
                .amount(amount)
                .ticketCount(ticketCount)
                .approvedAt(approvedAt)
                .build();
    }
}
