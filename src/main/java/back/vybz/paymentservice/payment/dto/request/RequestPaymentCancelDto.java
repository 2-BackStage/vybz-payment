package back.vybz.paymentservice.payment.dto.request;

import back.vybz.paymentservice.payment.vo.request.RequestPaymentCancelVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestPaymentCancelDto {

    private String cancelReason;

    @Builder
    public RequestPaymentCancelDto(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public static RequestPaymentCancelDto from(RequestPaymentCancelVo requestPaymentCancelVo) {
        return RequestPaymentCancelDto.builder()
                .cancelReason(requestPaymentCancelVo.getCancelReason())
                .build();
    }
}
