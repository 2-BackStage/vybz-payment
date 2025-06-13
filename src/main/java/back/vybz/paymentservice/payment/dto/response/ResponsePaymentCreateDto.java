package back.vybz.paymentservice.payment.dto.response;

import back.vybz.paymentservice.payment.vo.response.ResponsePaymentCreateVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponsePaymentCreateDto {

    private String checkoutUrl;

    private String orderId;

    @Builder
    public ResponsePaymentCreateDto(String checkoutUrl, String orderId) {
        this.checkoutUrl = checkoutUrl;
        this.orderId = orderId;
    }

    public static ResponsePaymentCreateDto from(ResponsePaymentCreateVo responsePaymentCreateVo) {
        return ResponsePaymentCreateDto.builder()
                .checkoutUrl(responsePaymentCreateVo.getCheckoutUrl())
                .orderId(responsePaymentCreateVo.getOrderId())
                .build();
    }

    public ResponsePaymentCreateVo toVo() {
        return ResponsePaymentCreateVo.builder()
                .checkoutUrl(checkoutUrl)
                .orderId(orderId)
                .build();
    }
}
