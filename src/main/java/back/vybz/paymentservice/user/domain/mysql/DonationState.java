package back.vybz.paymentservice.user.domain.mysql;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DonationState {
    DEPOSIT("입금"),
    WITHDRAW("출금");

    private final String state;

    @JsonValue
    public String getLabel() {
        return state;
    }

    @JsonCreator
    public static DonationState fromString(String value) {
        for (DonationState status : DonationState.values()) {
            if (status.state.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown SocialType: " + value);
    }
}
