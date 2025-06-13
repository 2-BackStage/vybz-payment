package back.vybz.paymentservice.payment.dto.request;

import back.vybz.paymentservice.payment.vo.request.RequestPaymentFailVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestPaymentFailDto {

    private String orderId;

    private String failCode;

    private String failReason;

    @Builder
    public RequestPaymentFailDto(String orderId, String failCode,
                                 String failReason) {
        this.orderId = orderId;
        this.failCode = failCode;
        this.failReason = failReason;
    }

    public static RequestPaymentFailDto from(RequestPaymentFailVo requestPaymentFailVo) {
        return RequestPaymentFailDto.builder()
                .orderId(requestPaymentFailVo.getOrderId())
                .failCode(requestPaymentFailVo.getFailCode())
                .failReason(requestPaymentFailVo.getFailReason())
                .build();
    }
}
