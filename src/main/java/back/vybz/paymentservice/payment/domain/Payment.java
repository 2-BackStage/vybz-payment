package back.vybz.paymentservice.payment.domain;

import back.vybz.paymentservice.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_uuid", length = 50)
    private String userUuid;

    // 결제 키
    @Column(name = "payment_key", unique = true, length = 200)
    private String paymentKey;

    // 결제 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PaymentStatus paymentStatus;

    // 주문 이름
    @Column(name = "order_name", nullable = false, length = 100)
    private String orderName;

    // 주문 ID
    @Column(name = "order_id", length = 100)
    private String orderId;

    // 결제 수단
    @Column(name = "method", nullable = false, length = 30)
    private String method;

    // 결제 금액
    @Column(name = "amount", nullable = false, length = 50)
    private Integer amount;

    // PG사 정기 결제용 빌링 키
    @Column(name = "toss_billing_key", length = 50)
    private String tossBillingKey;

    // 결제 실패 코드
    @Column(name = "fail_code", length = 50)
    private String failCode;

    // 결제 실패 이유
    @Column(name = "fail_reason", length = 200)
    private String failReason;

    // 결제 타입
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false, length = 20)
    private PaymentType paymentType;

    // 결제 요청 시각
    @Column(name = "requested_at")
    private LocalDateTime requestedAt;

    // 결제 승인 시각 (성공 시에만)
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    // 결제 취소 시각 (취소된 경우)
    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @Builder
    public Payment(Long id, String userUuid, String paymentKey,
                   PaymentStatus paymentStatus, String orderName, String orderId,
                   String method, Integer amount, String tossBillingKey,
                   String failCode, String failReason,
                   LocalDateTime requestedAt, LocalDateTime approvedAt,
                   LocalDateTime canceledAt, PaymentType paymentType) {
        this.id = id;
        this.userUuid =   userUuid;
        this.paymentKey = paymentKey;
        this.paymentStatus = paymentStatus;
        this.orderName = orderName;
        this.orderId = orderId;
        this.method = method;
        this.amount = amount;
        this.tossBillingKey = tossBillingKey;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
        this.canceledAt = canceledAt;
        this.failCode = failCode;
        this.failReason = failReason;
        this.paymentType = paymentType;
    }

    public void approve(String paymentKey, String method, LocalDateTime approvedAt) {
        this.paymentKey = paymentKey;
        this.method = method;
        this.paymentStatus = PaymentStatus.DONE;
        this.approvedAt = approvedAt;
    }

    public void markRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public void failPayment(String failCode, String failReason) {
        this.paymentStatus = PaymentStatus.ABORTED;
        this.failCode = failCode;
        this.failReason = failReason;
    }

    public void markAborted() {
        this.paymentStatus = PaymentStatus.ABORTED;
    }

    public void cancel() {
        this.paymentStatus = PaymentStatus.CANCELED;
        this.canceledAt = LocalDateTime.now();
    }

}
