package back.vybz.paymentservice.subscription.vo.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseBuskerProfileVo {

    private String nickname;

    @Builder
    public ResponseBuskerProfileVo(String nickname) {
        this.nickname = nickname;
    }
}
