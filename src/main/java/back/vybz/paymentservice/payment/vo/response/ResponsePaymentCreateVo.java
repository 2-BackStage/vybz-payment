package back.vybz.paymentservice.payment.vo.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponsePaymentCreateVo {

    private String checkoutUrl;

    private String orderId;

    @Builder
    public ResponsePaymentCreateVo(String checkoutUrl, String orderId) {
        this.checkoutUrl = checkoutUrl;
        this.orderId = orderId;
    }
}
