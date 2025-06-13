package back.vybz.paymentservice.payment.vo.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestPaymentCancelVo {

    private String cancelReason;

    @Builder
    public RequestPaymentCancelVo(String cancelReason) {
        this.cancelReason = cancelReason;
    }
}
