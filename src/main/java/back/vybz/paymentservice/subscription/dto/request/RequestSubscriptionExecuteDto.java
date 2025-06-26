package back.vybz.paymentservice.subscription.dto.request;

import back.vybz.paymentservice.subscription.vo.request.RequestSubscriptionExecuteVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestSubscriptionExecuteDto {

    private String customerKey;

    private String billingKey;

    private Integer price;

    private String orderName;

    private String userUuid;

    private String buskerUuid;

    @Builder
    public RequestSubscriptionExecuteDto(String customerKey, String billingKey, Integer price,
                                         String orderName, String userUuid, String buskerUuid) {
        this.customerKey = customerKey;
        this.billingKey = billingKey;
        this.price = price;
        this.orderName = orderName;
        this.userUuid = userUuid;
        this.buskerUuid = buskerUuid;
    }

    public static  RequestSubscriptionExecuteDto from(RequestSubscriptionExecuteVo requestSubscriptionExecuteVo) {
        return RequestSubscriptionExecuteDto.builder()
                .userUuid(requestSubscriptionExecuteVo.getUserUuid())
                .buskerUuid(requestSubscriptionExecuteVo.getBuskerUuid())
                .billingKey(requestSubscriptionExecuteVo.getBillingKey())
                .customerKey(requestSubscriptionExecuteVo.getCustomerKey())
                .price(requestSubscriptionExecuteVo.getPrice())
                .orderName(requestSubscriptionExecuteVo.getOrderName())
                .build();
    }
}
