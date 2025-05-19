package back.vybz.paymentservice.user.domain.mysql;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "refund_history")
@Getter
@NoArgsConstructor
public class RefundHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_uuid", nullable = false)
    private String userUuid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "reason")
    private String reason;

    @Column(name = "refund_amount", nullable = false)
    private Integer refundAmount;

    @Column(name = "refund_at")
    private LocalDateTime refundAt;

    @Column(name = "refunded_at", nullable = false)
    private LocalDateTime refundedAt;

    @Builder
    public RefundHistory(Long id, String userUuid, String name, String reason,
                         Integer refundAmount, LocalDateTime refundAt, LocalDateTime refundedAt) {
        this.id = id;
        this.userUuid = userUuid;
        this.name = name;
        this.reason = reason;
        this.refundAmount = refundAmount;
        this.refundAt = refundAt;
        this.refundedAt = refundedAt;
    }
}
