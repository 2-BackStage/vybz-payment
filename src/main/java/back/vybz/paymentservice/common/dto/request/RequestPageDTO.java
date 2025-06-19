package back.vybz.paymentservice.common.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class RequestPageDTO {

    @Builder.Default
    @Min(value = 1, message = "over 1")
    private int page = 1;

    @Builder.Default
    @Min(value = 10, message = "set over 10")
    @Max(value = 100, message = "cannot over 100")
    private int size = 10;
}
