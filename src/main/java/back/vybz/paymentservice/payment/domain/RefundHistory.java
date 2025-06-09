package back.vybz.paymentservice.payment.domain;

import back.vybz.paymentservice.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "refund_history")
@Getter
@NoArgsConstructor
public class RefundHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_uuid", nullable = false)
    private String userUuid;

    @Column(name = "payment_uuid", nullable = false)
    private String paymentUuid;

    // 결제 키
    @Column(name = "payment_key", nullable = false)
    private String paymentKey;

    // 주문 ID
    @Column(name = "order_id", nullable = false)
    private String orderId;

    // 환불 사유
    @Column(name = "reason", nullable = false, length = 200)
    private String reason;

    // 환불된 금액 (단위: 원)
    @Column(name = "refund_amount", nullable = false)
    private Integer refundAmount;

    // 실패 코드
    @Column(name = "fail_code", length = 50)
    private String failCode;

    // 환불 처리 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "refund_status", nullable = false)
    private RefundStatus refundStatus;

    // 환불 요청 발생 시점
    @Column(name = "refund_at")
    private LocalDateTime refundAt;

    // 환불 완료 시점
    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Builder
    public RefundHistory(Long id, String userUuid, String paymentUuid, String paymentKey,
                         String orderId, String reason, Integer refundAmount, String failCode,
                         RefundStatus refundStatus, LocalDateTime refundAt, LocalDateTime completedAt) {
        this.id = id;
        this.userUuid = userUuid;
        this.paymentUuid = paymentUuid;
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.reason = reason;
        this.refundAmount = refundAmount;
        this.failCode = failCode;
        this.refundStatus = refundStatus;
        this.refundAt = refundAt;
        this.completedAt = completedAt;
    }
}
