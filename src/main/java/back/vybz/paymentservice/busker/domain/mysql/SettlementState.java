package back.vybz.paymentservice.busker.domain.mysql;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SettlementState {

    PENDING("정산대기"),
    SETTLED("정산완료"),
    CANCELLED("정산취소");

    private final String description;

}
