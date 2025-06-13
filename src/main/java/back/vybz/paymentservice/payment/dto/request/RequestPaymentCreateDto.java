package back.vybz.paymentservice.payment.dto.request;

import back.vybz.paymentservice.payment.domain.Payment;
import back.vybz.paymentservice.payment.domain.PaymentStatus;
import back.vybz.paymentservice.payment.domain.PaymentType;
import back.vybz.paymentservice.payment.vo.request.RequestPaymentCreateVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class RequestPaymentCreateDto {

    private String userUuid;

    private Integer amount;

    private String orderId;

    private String method;

    private String orderName;

    private PaymentType paymentType;

    private PaymentStatus paymentStatus;


    @Builder
    public RequestPaymentCreateDto(String userUuid, Integer amount, String method, String orderId,
                                   String orderName,PaymentType paymentType, PaymentStatus paymentStatus) {
        this.userUuid = userUuid;
        this.amount = amount;
        this.method = method;
        this.orderId = orderId;
        this.orderName = orderName;
        this.paymentType = paymentType;
        this.paymentStatus = paymentStatus;
    }

    public static RequestPaymentCreateDto from(RequestPaymentCreateVo requestPaymentCreateVo) {
        return RequestPaymentCreateDto.builder()
                .userUuid(requestPaymentCreateVo.getUserUuid())
                .orderId(UUID.randomUUID().toString())
                .amount(requestPaymentCreateVo.getAmount())
                .method(requestPaymentCreateVo.getMethod())
                .orderName(requestPaymentCreateVo.getOrderName())
                .paymentType(requestPaymentCreateVo.getPaymentType())
                .paymentStatus(PaymentStatus.READY)
                .build();
    }

    public Payment toEntity() {
        return Payment.builder()
                .userUuid(userUuid)
                .amount(amount)
                .method(method)
                .orderName(orderName)
                .paymentType(paymentType)
                .paymentStatus(paymentStatus)
                .orderId(orderId)
                .build();
    }
}
