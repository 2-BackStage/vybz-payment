package back.vybz.paymentservice.subscription.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubscriptionStatus {

    READY("준비중"),
    ACTIVE("구독"),
    CANCELED("취소");

    private final String subscriptionStatus;
}
