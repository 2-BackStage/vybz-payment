package back.vybz.paymentservice.payment.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentType {

    CHARGE("v-티켓 충전"),
    MEMBERSHIP("멤버십");

    private final String paymentType;
}
