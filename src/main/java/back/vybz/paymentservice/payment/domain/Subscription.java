package back.vybz.paymentservice.payment.domain;

import back.vybz.paymentservice.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "subscription")
@Getter
@NoArgsConstructor
public class Subscription extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_uuid", nullable = false)
    private String userUuid;

    @Column(name = "busker_uuid", nullable = false)
    private String buskerUuid;

    @Column(name = "toss_billing_key", nullable = false)
    private String tossBillingKey;

    // 정기 결제 요금 (고정 가격)
    @Column(name = "price", nullable = false)
    private Integer price;

    // 결제 실패 횟수
    @Column(name = "fail_count", nullable = false)
    private Integer failCount = 0;

    // 구독 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SubscriptionStatus subscriptionStatus;

    // 구독 취소 시각
    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    // 마지막 결제 성공 시각
    @Column(name = "last_payment_at")
    private LocalDateTime lastPaymentAt;

    // 다음 결제 예정일
    @Column(name = "next_payment_at")
    private LocalDateTime nextPaymentAt;

    @Builder
    public Subscription(Long id, String userUuid, String buskerUuid, String tossBillingKey,
                        Integer price, Integer failCount, SubscriptionStatus subscriptionStatus,
                        LocalDateTime canceledAt, LocalDateTime lastPaymentAt, LocalDateTime nextPaymentAt) {
        this.id = id;
        this.userUuid = userUuid;
        this.buskerUuid = buskerUuid;
        this.tossBillingKey = tossBillingKey;
        this.price = price;
        this.failCount = failCount;
        this.subscriptionStatus = subscriptionStatus;
        this.canceledAt = canceledAt;
        this.lastPaymentAt = lastPaymentAt;
        this.nextPaymentAt = nextPaymentAt;
    }
}
