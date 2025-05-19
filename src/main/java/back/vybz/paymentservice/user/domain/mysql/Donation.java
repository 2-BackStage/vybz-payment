package back.vybz.paymentservice.user.domain.mysql;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "donation")
@Getter
@NoArgsConstructor
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_uuid", nullable = false)
    private String userUuid;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private DonationState donationState;

    @Builder
    public Donation(Long id, String userUuid, int amount, DonationState donationState) {
        this.id = id;
        this.userUuid = userUuid;
        this.amount = amount;
        this.donationState = donationState;
    }
}
