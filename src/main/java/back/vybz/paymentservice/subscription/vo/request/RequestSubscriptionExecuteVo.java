package back.vybz.paymentservice.subscription.vo.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestSubscriptionExecuteVo {

    private String customerKey;

    private String billingKey;

    private Integer price;

    private String orderName;

    private String userUuid;

    private String buskerUuid;

    @Builder
    public RequestSubscriptionExecuteVo(String customerKey, String billingKey, Integer price,
                                        String orderName, String userUuid, String buskerUuid) {
        this.customerKey = customerKey;
        this.billingKey = billingKey;
        this.price = price;
        this.orderName = orderName;
        this.userUuid = userUuid;
        this.buskerUuid = buskerUuid;
    }
}
