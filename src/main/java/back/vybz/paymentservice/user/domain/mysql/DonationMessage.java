package back.vybz.paymentservice.user.domain.mysql;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "donation_message")
@Getter
@NoArgsConstructor
public class DonationMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_uuid", nullable = false)
    private String userUuid;

    @Column(name = "busker_uuid", nullable = false)
    private String buskerUuid;

    @Column(name = "message")
    private String message;

    @Builder
    public DonationMessage(Long id, String userUuid, String buskerUuid, String message) {
        this.id = id;
        this.userUuid = userUuid;
        this.buskerUuid = buskerUuid;
        this.message = message;
    }
}
