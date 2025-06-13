package back.vybz.paymentservice.payment.dto.response;

import back.vybz.paymentservice.payment.domain.PaymentStatus;
import back.vybz.paymentservice.payment.vo.response.ResponsePaymentConfirmVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponsePaymentConfirmDto {

    private String userUuid;

    private String paymentKey;

    private PaymentStatus paymentStatus;

    private String message;

    private String orderId;

    private String orderName;

    private Integer amount;

    private String method;

    private String approvedAt;

    @Builder
    public ResponsePaymentConfirmDto(String userUuid, String paymentKey,
                                     PaymentStatus paymentStatus, String message, String orderId,
                                     String orderName, Integer amount,
                                     String method, String approvedAt) {
        this.userUuid = userUuid;
        this.paymentKey = paymentKey;
        this.paymentStatus = paymentStatus;
        this.message = message;
        this.orderId = orderId;
        this.orderName = orderName;
        this.amount = amount;
        this.method = method;
        this.approvedAt = approvedAt;
    }

    public static ResponsePaymentConfirmDto from(ResponsePaymentConfirmVo responsePaymentConfirmVo) {
        return ResponsePaymentConfirmDto.builder()
                .paymentKey(responsePaymentConfirmVo.getPaymentKey())
                .paymentStatus(responsePaymentConfirmVo.getPaymentStatus())
                .message(responsePaymentConfirmVo.getMessage())
                .orderId(responsePaymentConfirmVo.getOrderId())
                .orderName(responsePaymentConfirmVo.getOrderName())
                .amount(responsePaymentConfirmVo.getAmount())
                .method(responsePaymentConfirmVo.getMethod())
                .approvedAt(responsePaymentConfirmVo.getApprovedAt())
                .build();
    }

    public ResponsePaymentConfirmVo toVo() {
        return ResponsePaymentConfirmVo.builder()
                .paymentKey(paymentKey)
                .paymentStatus(paymentStatus)
                .message(message)
                .orderId(orderId)
                .orderName(orderName)
                .amount(amount)
                .method(method)
                .approvedAt(approvedAt)
                .build();
    }
}
