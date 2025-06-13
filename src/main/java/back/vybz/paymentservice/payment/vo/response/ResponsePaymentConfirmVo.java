package back.vybz.paymentservice.payment.vo.response;

import back.vybz.paymentservice.payment.domain.PaymentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponsePaymentConfirmVo {

    private String paymentKey;

    private PaymentStatus paymentStatus;

    private String message;

    private String orderId;

    private String orderName;

    private Integer amount;

    private String method;

    private String approvedAt;

    @Builder
    public ResponsePaymentConfirmVo(String paymentKey,
                                    PaymentStatus paymentStatus, String message,
                                    String orderId, String orderName, Integer amount,
                                    String method, String approvedAt) {
        this.paymentKey = paymentKey;
        this.paymentStatus = paymentStatus;
        this.message = message;
        this.orderId = orderId;
        this.orderName = orderName;
        this.amount = amount;
        this.method = method;
        this.approvedAt = approvedAt;
    }
}
