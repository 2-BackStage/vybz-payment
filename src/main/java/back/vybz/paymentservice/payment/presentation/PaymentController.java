package back.vybz.paymentservice.payment.presentation;

import back.vybz.paymentservice.common.entity.BaseResponseEntity;
import back.vybz.paymentservice.common.entity.BaseResponseStatus;
import back.vybz.paymentservice.payment.application.PaymentService;
import back.vybz.paymentservice.payment.dto.request.RequestPaymentCancelDto;
import back.vybz.paymentservice.payment.dto.request.RequestPaymentConfirmDto;
import back.vybz.paymentservice.payment.dto.request.RequestPaymentCreateDto;
import back.vybz.paymentservice.payment.dto.request.RequestPaymentFailDto;
import back.vybz.paymentservice.payment.dto.response.ResponsePaymentConfirmDto;
import back.vybz.paymentservice.payment.dto.response.ResponsePaymentCreateDto;
import back.vybz.paymentservice.payment.vo.request.RequestPaymentCancelVo;
import back.vybz.paymentservice.payment.vo.request.RequestPaymentConfirmVo;
import back.vybz.paymentservice.payment.vo.request.RequestPaymentCreateVo;
import back.vybz.paymentservice.payment.vo.request.RequestPaymentFailVo;
import back.vybz.paymentservice.payment.vo.response.ResponsePaymentConfirmVo;
import back.vybz.paymentservice.payment.vo.response.ResponsePaymentCreateVo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final PaymentService paymentService;

    // 결제 생성
    @PostMapping
    @Operation(summary = "결제 생성 API", description = "결제 생성(결제창 호출) API 입니다.", tags = {"Payment-Service"})
    public BaseResponseEntity<ResponsePaymentCreateVo> addPayment(
            @RequestBody RequestPaymentCreateVo requestPaymentCreateVo)
    {

        ResponsePaymentCreateDto responsePaymentCreateDto = paymentService.addPayment(RequestPaymentCreateDto.from(requestPaymentCreateVo));

        return new BaseResponseEntity<>(responsePaymentCreateDto.toVo());
    }

    // 결제 요청
    @PostMapping("/confirm")
    @Operation(summary = "결제 승인 API", description = "결제 승인 API 입니다.", tags = {"Payment-Service"})
    public BaseResponseEntity<ResponsePaymentConfirmVo> updatePayment(
            @RequestBody RequestPaymentConfirmVo requestPaymentConfirmVo
            ) {

        ResponsePaymentConfirmDto responsePaymentConfirmDto = paymentService.confirmPayment(RequestPaymentConfirmDto.from(requestPaymentConfirmVo));

        return new BaseResponseEntity<>(responsePaymentConfirmDto.toVo());
    }

    // 결제 실패
    @PostMapping("/fail")
    @Operation(summary = "결제 실패 API", description = "결제 실패 API 입니다.", tags = {"Payment-Service"})
    public BaseResponseEntity<Void> failPayment(@RequestBody RequestPaymentFailVo requestPaymentFailVo) {

        paymentService.markAsFailedPayment(RequestPaymentFailDto.from(requestPaymentFailVo));

        return new BaseResponseEntity<>(BaseResponseStatus.SUCCESS, "결제 실패 처리를 완료했습니다.");
    }

    // 환불 (= 결제 취소)
    @PostMapping("/{paymentKey}/cancel")
    @Operation(summary = "결제 취소 API", description = "결제 취소 API 입니다.", tags = {"Payment-Service"})
    public BaseResponseEntity<Void> cancelPayment(
            @PathVariable("paymentKey") String paymentKey,
            @RequestBody RequestPaymentCancelVo requestPaymentCancelVo
    ) {

        RequestPaymentCancelDto requestPaymentCancelDto = RequestPaymentCancelDto.from(requestPaymentCancelVo);
        paymentService.cancelPayment(paymentKey, requestPaymentCancelDto);

        return new BaseResponseEntity<>(BaseResponseStatus.SUCCESS, "결제 취소 되었습니다.");
    }
}
