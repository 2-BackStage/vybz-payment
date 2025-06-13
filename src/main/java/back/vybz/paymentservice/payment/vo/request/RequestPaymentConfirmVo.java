package back.vybz.paymentservice.payment.vo.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RequestPaymentConfirmVo {

    private String userUuid;

    private String paymentKey;

    private String orderId;

    private Integer amount;

    private LocalDateTime requestedAt;
}
