package back.vybz.paymentservice.payment.vo.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestPaymentFailVo {

    private String orderId;

    private String failCode;

    private String failReason;

    @Builder
    public RequestPaymentFailVo(String orderId, String failCode,
                                 String failReason) {
        this.orderId = orderId;
        this.failCode = failCode;
        this.failReason = failReason;
    }
}
