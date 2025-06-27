package back.vybz.paymentservice.subscription.vo.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseBillingKeyVo {

    private String billingKey;

    @Builder
    public ResponseBillingKeyVo(String billingKey) {
        this.billingKey = billingKey;
    }
}
