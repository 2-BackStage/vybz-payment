package back.vybz.paymentservice.user.domain.mysql;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentHistoryStatus {

    PENDING("대기"),
    SUCCESS("성공"),
    FAILED("실패");

    private final String description;

    @JsonValue
    public String getLabel() {
        return description;
    }

    @JsonCreator
    public static PaymentHistoryStatus fromString(String value) {
        for (PaymentHistoryStatus status : PaymentHistoryStatus.values()) {
            if (status.description.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown SocialType: " + value);
    }
}
