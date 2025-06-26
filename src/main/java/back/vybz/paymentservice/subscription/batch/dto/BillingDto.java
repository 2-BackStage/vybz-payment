package back.vybz.paymentservice.subscription.batch.dto;

import back.vybz.paymentservice.subscription.domain.Subscription;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class BillingDto {

    private String userUuid;

    private String buskerUuid;

    private String tossBillingKey;

    private String customerKey;

    private Integer price;

    private LocalDateTime nextPaymentAt;

    private String orderId;

    private String orderName;

    @Builder
    public BillingDto(String userUuid, String buskerUuid, String tossBillingKey, String customerKey,
                      Integer price, LocalDateTime nextPaymentAt, String orderId, String orderName) {
        this.userUuid = userUuid;
        this.buskerUuid = buskerUuid;
        this.tossBillingKey = tossBillingKey;
        this.customerKey = customerKey;
        this.price = price;
        this.nextPaymentAt = nextPaymentAt;
        this.orderId = orderId;
        this.orderName = orderName;
    }

    public static BillingDto fromSubscription(Subscription subscription) {
        return BillingDto.builder()
                .userUuid(subscription.getUserUuid())
                .buskerUuid(subscription.getBuskerUuid())
                .tossBillingKey(subscription.getTossBillingKey())
                .customerKey(subscription.getCustomerKey())
                .price(subscription.getPrice())
                .nextPaymentAt(subscription.getNextPaymentAt())
                .orderId(UUID.randomUUID().toString())
                .orderName("멤버십")
                .build();
    }
}
