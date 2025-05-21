package back.vybz.paymentservice.busker.domain.mongodb;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@NoArgsConstructor
@Document("donation_received")
public class DonationReceivedMongo {

    @Id
    private ObjectId id;

    // 버스커 uuid
    @Field(name = "busker_uuid")
    private String buskerUuid;

    // 후원한 유저
    @Field(name = "donation_user")
    private List<DonationUser> donationUser;

    // 총 후원 받은 금액
    @Field(name = "total_received_amount")
    private Long totalReceivedAmount;

}
