package back.vybz.paymentservice.busker.domain.mysql;

import back.vybz.paymentservice.common.entity.SoftDeletableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import back.vybz.paymentservice.common.entity.BaseEntity;

@Entity
@Getter
@Table(name = "donation_received")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DonationReceived extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 후원 받은 내역 uuid
     */
    @Column(name = "donation_received_uuid", nullable = false, unique = true)
    private String donationReceivedUuid;

    /**
     * 후원자 uuid
     */
    @Column(name = "user_uuid", nullable = false)
    private String userUuid;

    /**
     * 버스커 uuid
     */
    @Column(name = "busker_uuid", nullable = false)
    private String buskerUuid;

    /**
     * 후원 금액
     */
    @Column(name = "amount", nullable = false)
    private Integer amount;

    /**
     * 후원 메시지
     */
    @Column(name = "message", length = 200)
    private String message;

}
