package back.vybz.paymentservice.payment.application;

import back.vybz.paymentservice.common.entity.BaseResponseStatus;
import back.vybz.paymentservice.common.exception.BaseException;
import back.vybz.paymentservice.payment.domain.Payment;
import back.vybz.paymentservice.payment.domain.PaymentStatus;
import back.vybz.paymentservice.payment.domain.RefundHistory;
import back.vybz.paymentservice.payment.domain.RefundStatus;
import back.vybz.paymentservice.payment.dto.request.RequestPaymentCancelDto;
import back.vybz.paymentservice.payment.dto.request.RequestPaymentConfirmDto;
import back.vybz.paymentservice.payment.dto.request.RequestPaymentCreateDto;
import back.vybz.paymentservice.payment.dto.request.RequestPaymentFailDto;
import back.vybz.paymentservice.payment.dto.response.ResponsePaymentConfirmDto;
import back.vybz.paymentservice.payment.dto.response.ResponsePaymentCreateDto;
import back.vybz.paymentservice.payment.infrastructure.PaymentRepository;
import back.vybz.paymentservice.payment.infrastructure.RefundHistoryRepository;
import back.vybz.paymentservice.payment.util.TossHeaderHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final RefundHistoryRepository refundHistoryRepository;

    private final TossHeaderHelper tossHeaderHelper;

    private final RestTemplate restTemplate;

    private static final int TICKET_UNIT_PRICE = 110;

    @Value("${payment.base-url}")
    private String baseUrl;

    @Value("${payment.success-url}")
    private String successUrl;

    @Value("${payment.fail-url}")
    private String failUrl;

    // 결제 생성
    @Override
    public ResponsePaymentCreateDto addPayment(RequestPaymentCreateDto requestPaymentCreateDto) {

        // 요청된 정보로 Payment 엔티티 생성
        Payment payment = requestPaymentCreateDto.toEntity();
        payment.markRequestedAt(LocalDateTime.now());

        // 요청된 정보 DB 1차 저장
        paymentRepository.save(payment);

        // Basic 인증 설정 (secretKey:는 toss 규약)
        HttpHeaders headers = tossHeaderHelper.createAuthHeaders();

        // Toss 결제 생성 요청 바디 구성
        Map<String, Object> body = new HashMap<>();

        body.put("method", payment.getMethod());              // 결제 수단
        body.put("orderId", payment.getOrderId());            // 주문 번호
        body.put("amount", payment.getAmount());              // 결제 금액
        body.put("orderName", payment.getOrderName());        // 주문명
        body.put("successUrl", successUrl);                   // 결제 성공 후 리디렉션 URL
        body.put("failUrl", failUrl);                         // 결제 실패 시 리디렉션 URL

        // 옵션 설정
        body.put("validHours", 1);                           // 유효시간 (1시간)

        HttpEntity<Map<String, Object>> httpRequest = new HttpEntity<>(body, headers);

        // Toss 결제 생성 API 호출
        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl, httpRequest, Map.class);

        Map responseBody = response.getBody();

        // checkoutUrl 추출 (Toss 결제 페이지 링크)
        Object checkoutObj = responseBody.get("checkout");

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> checkout = objectMapper.convertValue(checkoutObj, new TypeReference<>() {});

        if (checkout == null || !checkout.containsKey("url")) {
            throw new BaseException(BaseResponseStatus.TOSS_EMPTY_RESPONSE);
        }
        String checkoutUrl = (String) checkout.get("url");

        // 사용자에게 응답 반환 (주문번호 + Toss 결제 링크)
        return ResponsePaymentCreateDto.builder()
                .checkoutUrl(checkoutUrl)
                .orderId(payment.getOrderId())
                .build();
    }

    @Transactional
    @Override
    public ResponsePaymentConfirmDto confirmPayment(RequestPaymentConfirmDto requestPaymentConfirmDto) {

        HttpHeaders headers = tossHeaderHelper.createAuthHeaders();

        // Toss 결제 승인 API 필요한 바디 구성
        Map<String, Object> body = new HashMap<>();

        body.put("paymentKey", requestPaymentConfirmDto.getPaymentKey());
        body.put("orderId", requestPaymentConfirmDto.getOrderId());
        body.put("amount", requestPaymentConfirmDto.getAmount());

        // 요청 바디와 헤더를 HttpEntity 감싸기
        HttpEntity<Map<String, Object>> httpRequest = new HttpEntity<>(body, headers);

        // DB 결제 내역 조회
        Payment payment = paymentRepository
                .findByOrderId(requestPaymentConfirmDto.getOrderId())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PAYMENT_NO_EXIST));

        // 이미 승인된 결제인지 확인
        if (payment.getPaymentStatus() != PaymentStatus.READY) {
            throw new BaseException(BaseResponseStatus.PAYMENT_ALREADY_DONE);
        }

        try {
            // Toss 결제 승인 API 호출
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    baseUrl + "/confirm", httpRequest, Map.class
            );

            Map responseBody = response.getBody();

            if (responseBody == null) {
                throw new BaseException(BaseResponseStatus.TOSS_EMPTY_RESPONSE);
            }

            // 응답에서 필요한 정보 추출
            String paymentKey = (String) responseBody.get("paymentKey");
            String orderId = (String) responseBody.get("orderId");
            String method = (String) responseBody.get("method");
            Integer amount = (Integer) responseBody.get("totalAmount");
            PaymentStatus paymentStatus = PaymentStatus.valueOf((String) responseBody.get("status"));

            Map<String, Object> failure = (Map<String, Object>) responseBody.get("failure");

            // 가상 결제의 경우 approvedAt이 null일 수 있음
            LocalDateTime approvedAt = Optional.ofNullable((String) responseBody.get("approvedAt"))
                    .map(OffsetDateTime::parse)
                    .map(OffsetDateTime::toLocalDateTime)
                    .orElse(null);

            // 실패 응답이 온 경우 → 상태 업데이트 후 예외 반환
            if (failure != null) {

                String failCode = (String) failure.get("code");
                String failReason = (String) failure.get("message");

                RequestPaymentFailDto requestPaymentFailDto = RequestPaymentFailDto.builder()
                        .orderId(orderId)
                        .failCode(failCode)
                        .failReason(failReason)
                        .build();

                markAsFailedPayment(requestPaymentFailDto);

                throw new BaseException(BaseResponseStatus.TOSS_PAYMENT_REJECTED);
            }

            // 금액 불일치 시 검증
            if (!Objects.equals(amount, payment.getAmount())) {
                throw new BaseException(BaseResponseStatus.PAYMENT_AMOUNT_MISMATCH);
            }

            // 결제 승인 성공 시 결제 상태 업데이트
            payment.approve(paymentKey, method, approvedAt);

            int ticketCount = payment.getAmount() / TICKET_UNIT_PRICE;

            System.out.println("📂 ticketCount 확인:  " + ticketCount);

            // TODO: kafka producer 처리 예정

            return ResponsePaymentConfirmDto.builder()
                    .paymentKey(paymentKey)
                    .message(paymentStatus.getDescription())
                    .orderId(orderId)
                    .paymentStatus(paymentStatus)
                    .approvedAt(approvedAt != null ? approvedAt.toString() : null)
                    .method(method)
                    .orderName(payment.getOrderName())
                    .amount(payment.getAmount())
                    .build();

        } catch (HttpClientErrorException e) {

            String errorBody = e.getResponseBodyAsString();
            log.error("[Toss 결제 오류] {}", errorBody);

            throw new BaseException(BaseResponseStatus.TOSS_API_CALL_FAIL); // Toss API 호출 자체 실패
        }
    }

    /**
     * 환불 (= 결제 취소)
     *
     * @param paymentKey
     * @param requestPaymentCancelDto
     */
    @Transactional
    @Override
    public void cancelPayment(String paymentKey,RequestPaymentCancelDto requestPaymentCancelDto) {

        Payment payment = paymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PAYMENT_NOT_FOUND));

        // 이미 결제 상태가 CANCELED 경우
        if (payment.getPaymentStatus() == PaymentStatus.CANCELED) {
            throw new BaseException(BaseResponseStatus.PAYMENT_ALREADY_CANCELED);
        }

        // 결제 상태가 DONE 아닐 경우 (미승인 상태)
        if (payment.getPaymentStatus() != PaymentStatus.DONE) {
            throw new BaseException(BaseResponseStatus.PAYMENT_NOT_DONE);
        }

        // 환불 이력 존재 여부 확인
        if(refundHistoryRepository.existsByPaymentKey(paymentKey)) {
            throw new BaseException(BaseResponseStatus.PAYMENT_CANCEL_ALREADY_REQUESTED);
        }

        HttpHeaders headers = tossHeaderHelper.createAuthHeaders();

        Map<String, Object> body = new HashMap<>();

        body.put("cancelReason", requestPaymentCancelDto.getCancelReason());

        HttpEntity<Map<String, Object>> httpRequest = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl + "/{paymentKey}/cancel", httpRequest, Map.class, paymentKey
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            Map responseBody = response.getBody();

            List<Map<String, Object>> cancels = (List<Map<String, Object>>) responseBody.get("cancels");

            Map<String, Object> cancelData = cancels != null && !cancels.isEmpty() ? cancels.get(0) : null;

            Integer refundAmount = cancelData != null && cancelData.get("cancelAmount") != null
                    ? ((Number) cancelData.get("cancelAmount")).intValue()
                    : 0;

            payment.cancel();

            // 환불 내역 저장
            RefundHistory refundHistory = RefundHistory.builder()
                    .userUuid(payment.getUserUuid())
                    .paymentKey(payment.getPaymentKey())
                    .orderId(payment.getOrderId())
                    .refundAmount(refundAmount)
                    .cancelReason(requestPaymentCancelDto.getCancelReason())
                    .refundStatus(RefundStatus.COMPLETED)
                    .refundAt(LocalDateTime.now())
                    .completedAt(LocalDateTime.now())
                    .build();

            refundHistoryRepository.save(refundHistory);
        } else {
            throw new BaseException(BaseResponseStatus.TOSS_API_CALL_FAIL);
        }
    }

    @Transactional
    public void markAsFailedPayment(RequestPaymentFailDto requestPaymentFailDto) {
        Payment payment = paymentRepository.findByOrderId(requestPaymentFailDto.getOrderId())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PAYMENT_NO_EXIST));

        payment.failPayment(requestPaymentFailDto.getFailCode(), requestPaymentFailDto.getFailReason());
    }
}
