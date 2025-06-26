package back.vybz.paymentservice.subscription.dto.request;

import back.vybz.paymentservice.subscription.vo.request.RequestSubscriptionCreateVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestSubscriptionCreateDto {

    private String userUuid;

    private String buskerUuid;

    private Integer price;

    private String authKey;

    private String customerKey;

    @Builder
    public RequestSubscriptionCreateDto(String userUuid, String buskerUuid, Integer price,
                                        String authKey, String customerKey) {
        this.userUuid = userUuid;
        this.buskerUuid = buskerUuid;
        this.price = price;
        this.authKey = authKey;
        this.customerKey = customerKey;
    }

    public static RequestSubscriptionCreateDto from(RequestSubscriptionCreateVo requestSubscriptionCreateVo) {
        return RequestSubscriptionCreateDto.builder()
                .userUuid(requestSubscriptionCreateVo.getUserUuid())
                .buskerUuid(requestSubscriptionCreateVo.getBuskerUuid())
                .price(requestSubscriptionCreateVo.getPrice())
                .authKey(requestSubscriptionCreateVo.getAuthKey())
                .customerKey(requestSubscriptionCreateVo.getCustomerKey())
                .build();
    }
}
