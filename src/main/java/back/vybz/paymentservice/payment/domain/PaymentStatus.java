package back.vybz.paymentservice.payment.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {

    READY("준비"),
    IN_PROGRESS("진행 중"),
    WAITING_FOR_DEPOSIT("입금 대기 중"),
    DONE("결제 완료"),
    CANCELED("취소됨"),
    PARTIAL_CANCELED("부분 취소됨"),
    ABORTED("승인 실패"),
    EXPIRED("만료됨");

    private final String description;
}
