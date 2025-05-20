package back.vybz.paymentservice.settlements.domain.mysql;

import lombok.Getter;

@Getter
public enum SettlementState {
    PENDING("대기 중"),
    DENIED("거절됨"),
    APPROVED("승인됨");

    private final String description;

    SettlementState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

