package back.vybz.paymentservice.payment.vo.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponsePaymentCancelVo {

    private String paymentKey;

    private String failCode;

    private String failReason;

    @Builder
    public ResponsePaymentCancelVo( String paymentKey, String failCode, String failReason) {
        this.paymentKey = paymentKey;
        this.failCode = failCode;
        this.failReason = failReason;
    }
}
