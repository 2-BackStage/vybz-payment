package back.vybz.paymentservice.subscription.domain;

import back.vybz.paymentservice.common.entity.SoftDeletableEntity;
import back.vybz.paymentservice.subscription.batch.policy.BillingPaymentPolicy;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Table(name = "subscription")
@Getter
@NoArgsConstructor
public class Subscription extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_uuid", nullable = false)
    private String userUuid;

    @Column(name = "busker_uuid", nullable = false)
    private String buskerUuid;

    // toss billingKey
    @Column(name = "toss_billing_key", nullable = false)
    private String tossBillingKey;

    // 고객 식별 키
    @Column(name = "customer_key", nullable = false)
    private String customerKey;

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
    public Subscription(Long id, String userUuid, String buskerUuid, String tossBillingKey, String customerKey,
                        Integer price, Integer failCount, SubscriptionStatus subscriptionStatus,
                        LocalDateTime canceledAt, LocalDateTime lastPaymentAt, LocalDateTime nextPaymentAt) {
        this.id = id;
        this.userUuid = userUuid;
        this.buskerUuid = buskerUuid;
        this.tossBillingKey = tossBillingKey;
        this.customerKey = customerKey;
        this.price = price;
        this.failCount = (failCount != null) ? failCount : 0;
        this.subscriptionStatus = subscriptionStatus;
        this.canceledAt = canceledAt;
        this.lastPaymentAt = lastPaymentAt;
        this.nextPaymentAt = nextPaymentAt;
    }

    private static final ZoneId ZONE = ZoneId.of("Asia/Seoul");

    public void activate() {
        this.subscriptionStatus = SubscriptionStatus.ACTIVE;
        this.failCount = 0;
        this.lastPaymentAt = ZonedDateTime.now(ZONE).toLocalDateTime();
        this.nextPaymentAt = ZonedDateTime.now(ZONE).plusMonths(1).toLocalDateTime();
    }

    public void increaseFailCount() {
        if(this.subscriptionStatus == SubscriptionStatus.CANCELED) {
            return;
        }

        this.failCount++;

        // 재시도 간격 : 1일 후
        this.nextPaymentAt = BillingPaymentPolicy.calculateNextRetryAt(ZonedDateTime.now(ZONE).toLocalDateTime());

        if (BillingPaymentPolicy.isRetryExceeded(this.failCount)) {
            this.subscriptionStatus = SubscriptionStatus.CANCELED;
            this.canceledAt = ZonedDateTime.now(ZONE).toLocalDateTime();
        }
    }

    public void updateNextPaymentAt(LocalDateTime base) {
        this.nextPaymentAt = BillingPaymentPolicy.calculateNextPaymentAt(base);
    }

    public void resetFailCount() {
        this.failCount = 0;
    }

    public void cancel() {
        this.subscriptionStatus = SubscriptionStatus.CANCELED;
        this.canceledAt = ZonedDateTime.now(ZONE).toLocalDateTime();
        this.softDelete();
    }
}
