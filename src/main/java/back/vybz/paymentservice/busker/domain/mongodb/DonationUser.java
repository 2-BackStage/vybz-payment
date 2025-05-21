package back.vybz.paymentservice.busker.domain.mongodb;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
public class DonationUser {

    // 유저 uuid
    private String userUuid;

    // 유저 닉네임
    private String nickName;

    // 후원 금액
    private Long amount;

    // 후원 메시지
    private String message;

    // 후원 날짜
    private Instant receivedAt;

}
