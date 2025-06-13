package back.vybz.paymentservice.payment.application;

import back.vybz.paymentservice.payment.dto.request.RequestPaymentCancelDto;
import back.vybz.paymentservice.payment.dto.request.RequestPaymentConfirmDto;
import back.vybz.paymentservice.payment.dto.request.RequestPaymentCreateDto;
import back.vybz.paymentservice.payment.dto.request.RequestPaymentFailDto;
import back.vybz.paymentservice.payment.dto.response.ResponsePaymentConfirmDto;
import back.vybz.paymentservice.payment.dto.response.ResponsePaymentCreateDto;

public interface PaymentService {

    ResponsePaymentCreateDto addPayment(RequestPaymentCreateDto requestPaymentCreateDto);

    ResponsePaymentConfirmDto confirmPayment(RequestPaymentConfirmDto requestPaymentConfirmDto);

    void markAsFailedPayment(RequestPaymentFailDto requestPaymentFailDto);

    void cancelPayment(String paymentKey, RequestPaymentCancelDto requestPaymentCancelDto);
}
