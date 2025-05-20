package back.vybz.paymentservice.user.domain.mysql;

import back.vybz.paymentservice.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "member_ship")
@Getter
@NoArgsConstructor
public class MemberShip extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_uuid", nullable = false)
    private String userUuid;

    @Column(name = "paln_name", nullable = false)
    private String planName;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "status", nullable = false)
    private boolean memberShipStatus;

    @Column(name = "next_billing_date", nullable = false)
    private LocalDateTime nextBillingDate;

    @Column(name = "toss_billing_key", nullable = false)
    private String tossBillingKey;

    @Builder
    public MemberShip(Long id, String userUuid, String planName, Integer price,
                      boolean memberShipStatus, LocalDateTime nextBillingDate, String tossBillingKey) {
        this.id = id;
        this.userUuid = userUuid;
        this.planName = planName;
        this.price = price;
        this.memberShipStatus = memberShipStatus;
        this.nextBillingDate = nextBillingDate;
        this.tossBillingKey = tossBillingKey;
    }
}
