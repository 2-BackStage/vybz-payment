package back.vybz.paymentservice.user.domain.mysql;

import back.vybz.paymentservice.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@NoArgsConstructor
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_uuid", nullable = false)
    private String userUuid;

    @Column(name = "payment_uuid", nullable = false, unique = true)
    private String paymentUuid;

    @Column(name = "payment_code", nullable = false)
    private String paymentCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name = "order_name", nullable = false)
    private String orderName;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "method", nullable = false)
    private String method;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @Column(name = "fail_code")
    private String failCode;

    @Builder
    public Payment(Long id, String userUuid, String paymentUuid,
                   String paymentCode, PaymentStatus paymentStatus, String orderName,
                   String orderId, String method, Integer amount,
                   LocalDateTime requestedAt, LocalDateTime approvedAt, LocalDateTime canceledAt, String failCode) {
        this.id = id;
        this.userUuid = userUuid;
        this.paymentUuid = paymentUuid;
        this.paymentCode = paymentCode;
        this.paymentStatus = paymentStatus;
        this.orderName = orderName;
        this.orderId = orderId;
        this.method = method;
        this.amount = amount;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
        this.canceledAt = canceledAt;
        this.failCode = failCode;
    }
}
