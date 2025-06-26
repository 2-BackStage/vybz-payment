package back.vybz.paymentservice.subscription.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class ResponseBillingKeyDto {

    private String billingKey;

    @Builder
    public ResponseBillingKeyDto(String billingKey) {
        this.billingKey = billingKey;
    }
}
