package back.vybz.paymentservice.subscription.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestTossBillingKeyDto {

    private String authKey;

    private String customerKey;

    @Builder
    public RequestTossBillingKeyDto(String authKey, String customerKey) {
        this.authKey = authKey;
        this.customerKey = customerKey;
    }
}
