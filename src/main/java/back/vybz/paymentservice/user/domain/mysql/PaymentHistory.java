package back.vybz.paymentservice.user.domain.mysql;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment_history")
@Getter
@NoArgsConstructor
public class PaymentHistory {

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
    private PaymentHistoryStatus paymentHistoryStatus;

    @Column(name = "order_name", nullable = false)
    private String orderName;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "method", nullable = false)
    private String method;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Builder
    public PaymentHistory(Long id, String userUuid, String paymentUuid, String paymentCode,
                          PaymentHistoryStatus paymentHistoryStatus, String orderName,
                          String orderId, String method, Integer amount) {
        this.id = id;
        this.userUuid = userUuid;
        this.paymentUuid = paymentUuid;
        this.paymentCode = paymentCode;
        this.paymentHistoryStatus = paymentHistoryStatus;
        this.orderName = orderName;
        this.orderId = orderId;
        this.method = method;
        this.amount = amount;
    }
}
