package back.vybz.paymentservice.payment.vo.request;

import back.vybz.paymentservice.payment.domain.PaymentStatus;
import back.vybz.paymentservice.payment.domain.PaymentType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestPaymentCreateVo {

    private String userUuid;

    private Integer amount;

    private String method;

    private String orderName;

    private PaymentStatus paymentStatus;

    private PaymentType paymentType;
}