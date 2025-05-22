package back.vybz.paymentservice.busker.domain.mysql;

import back.vybz.paymentservice.common.entity.BaseEntity;
import back.vybz.paymentservice.common.entity.SoftDeletableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "settlement")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Settlement extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 버스커 uuid
     */
    @Column(name = "user_uuid", nullable = false)
    private String userUuid;

    /**
     * 정산 uuid
     */
    @Column(name = "settlement_uuid", nullable = false, unique = true)
    private String settlementUuid;

    /**
     * 예금주 이름
     */
    @Column(name = "deposit_name", nullable = false)
    private String depositName;

    /**
     * 은행 이름
     */
    @Column(name = "bank_name", nullable = false)
    private String bankName;

    /**
     * 계좌 번호(암호화)
     */
    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    /**
     * 정산 금액
     */
    @Column(name = "amount", nullable = false)
    private Integer amount;

    /**
     * 정산 상태
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private SettlementState state;

    /**
     * 거절 사유
     */
    @Column(name = "reason")
    private String reason;

    @Builder
    public Settlement(Long id, String userUuid, String settlementUuid, String depositName, String bankName,
                      String accountNumber, Integer amount, SettlementState state, String reason) {
        this.id = id;
        this.userUuid = userUuid;
        this.settlementUuid = settlementUuid;
        this.depositName = depositName;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.state = state;
        this.reason = reason;
    }

}
