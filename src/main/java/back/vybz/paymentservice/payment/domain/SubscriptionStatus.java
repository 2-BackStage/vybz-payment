package back.vybz.paymentservice.payment.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubscriptionStatus {

    ACTIVE("승인"),
    CANCELED("취소");

    private final String subscriptionStatus;
}
