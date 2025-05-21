package back.vybz.paymentservice.user.domain.mongodb;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Getter
@NoArgsConstructor
@Document("donation")
public class Donation {

    @Id
    private ObjectId id;

    @Field(name = "user_uuid")
    private String userUuid;

    @Field(name = "busker_nickname")
    private String buskerNickname;

    @Field(name = "amount")
    private Integer amount;

    @Field(name = "message")
    private String message;

    @Field(name = "donated_at")
    private Instant donatedAt;

    @Builder
    public Donation(ObjectId id, String userUuid, String buskerNickname,
                    Integer amount, String message, Instant donatedAt) {
        this.id = id;
        this.userUuid = userUuid;
        this.buskerNickname = buskerNickname;
        this.amount = amount;
        this.message = message;
        this.donatedAt = donatedAt;
    }
}
