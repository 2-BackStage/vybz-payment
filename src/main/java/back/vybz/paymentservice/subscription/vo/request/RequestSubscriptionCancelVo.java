package back.vybz.paymentservice.subscription.vo.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestSubscriptionCancelVo {

    private String userUuid;

    private String buskerUuid;

    @Builder
    public RequestSubscriptionCancelVo(String userUuid, String buskerUuid) {
        this.userUuid = userUuid;
        this.buskerUuid = buskerUuid;
    }
}
