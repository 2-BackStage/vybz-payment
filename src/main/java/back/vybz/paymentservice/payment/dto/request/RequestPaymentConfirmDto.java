package back.vybz.paymentservice.payment.dto.request;

import back.vybz.paymentservice.payment.domain.Payment;
import back.vybz.paymentservice.payment.vo.request.RequestPaymentConfirmVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RequestPaymentConfirmDto {

    private String userUuid;

    private String paymentKey;

    private String orderId;

    private Integer amount;

    private LocalDateTime requestedAt;

    @Builder
    public RequestPaymentConfirmDto(String userUuid, String paymentKey,
                                    String orderId, Integer amount, LocalDateTime requestedAt) {
        this.userUuid = userUuid;
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
        this.requestedAt = requestedAt;
    }

    public static RequestPaymentConfirmDto from(RequestPaymentConfirmVo requestPaymentConfirmVo) {
        return RequestPaymentConfirmDto.builder()
                .userUuid(requestPaymentConfirmVo.getUserUuid())
                .paymentKey(requestPaymentConfirmVo.getPaymentKey())
                .orderId(requestPaymentConfirmVo.getOrderId())
                .amount(requestPaymentConfirmVo.getAmount())
                .requestedAt(requestPaymentConfirmVo.getRequestedAt())
                .build();
    }

    public Payment toEntity() {
        return Payment.builder()
                .userUuid(userUuid)
                .amount(amount)
                .orderId(orderId)
                .paymentKey(paymentKey)
                .requestedAt(requestedAt)
                .build();
    }
}
