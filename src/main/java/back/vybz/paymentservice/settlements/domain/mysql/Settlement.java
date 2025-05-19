package back.vybz.paymentservice.settlements.domain.mysql;

import back.vybz.paymentservice.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "settlements")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Settlement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* 버스커 UUID */
    @Column(name = "user_uuid", nullable = false, length = 255)
    private String userUuid;

    /* 정산 UUID */
    @Column(name = "settlements_uuid", nullable = false, length = 255)
    private String settlementsUuid;

    /* 예금주 */
    @Column(name = "account_holder", nullable = false, length = 255)
    private String accountHolder;

    /* 은행사 */
    @Column(name = "bank", nullable = false, length = 20)
    private String bank;

    /* 계좌번호 (암호화 대상) */
    @Column(name = "account_number", nullable = false, length = 255)
    private String accountNumber;

    /* 정산 금액 */
    @Column(nullable = false)
    private int amount;

    /* 상태 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SettlementState state;

    /* 거절 사유 */
    @Column(length = 255)
    private String reason;

    @Builder
    public Settlement(String userUuid, String settlementsUuid, String accountHolder,
                      String bank, String accountNumber, int amount,
                      SettlementState state, String reason) {
        this.userUuid = userUuid;
        this.settlementsUuid = settlementsUuid;
        this.accountHolder = accountHolder;
        this.bank = bank;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.state = state;
        this.reason = reason;
    }
}

