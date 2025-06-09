package back.vybz.paymentservice.payment.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RefundStatus {

    PENDING("환불 대기 중"),
    COMPLETED("환불 완료"),
    FAILED("환불 실패");

    private final String refundStatus;
}
