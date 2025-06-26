package back.vybz.paymentservice.subscription.presentation;

import back.vybz.paymentservice.common.entity.BaseResponseEntity;
import back.vybz.paymentservice.common.entity.BaseResponseStatus;
import back.vybz.paymentservice.subscription.application.SubscriptionService;
import back.vybz.paymentservice.subscription.dto.request.RequestSubscriptionCancelDto;
import back.vybz.paymentservice.subscription.dto.request.RequestSubscriptionCreateDto;
import back.vybz.paymentservice.subscription.dto.request.RequestSubscriptionExecuteDto;
import back.vybz.paymentservice.subscription.dto.response.ResponseBillingKeyDto;
import back.vybz.paymentservice.subscription.vo.request.RequestSubscriptionCancelVo;
import back.vybz.paymentservice.subscription.vo.request.RequestSubscriptionCreateVo;
import back.vybz.paymentservice.subscription.vo.request.RequestSubscriptionExecuteVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/membership")
public class SubscriptionController {

    private final SubscriptionService membershipService;

    // billingKey 등록
    @PostMapping("/billing/register")
    public BaseResponseEntity<ResponseBillingKeyDto> issueBillingKey(
            @RequestBody RequestSubscriptionCreateVo requestSubscriptionCreateVo) {

        ResponseBillingKeyDto response = membershipService.registerBillingKey(RequestSubscriptionCreateDto.from(requestSubscriptionCreateVo));

        log.info("✅ ResponseBillingKeyDto: {}", response);

        return new BaseResponseEntity<>(BaseResponseStatus.SUCCESS, response);
    }

    // billingKey 결제
    @PostMapping("/billing/execute")
    public BaseResponseEntity<Void> executeBillingPayment (
            @RequestBody RequestSubscriptionExecuteVo requestSubscriptionExecuteVo
    ) {

        membershipService.executeBillingPayment(RequestSubscriptionExecuteDto.from(requestSubscriptionExecuteVo));

        return new BaseResponseEntity<>(BaseResponseStatus.SUCCESS);
    }

    @DeleteMapping
    public BaseResponseEntity<Void> cancelBillingPayment(
            @RequestBody RequestSubscriptionCancelVo requestSubscriptionCancelVo
    ) {
        membershipService.cancelSubscription(RequestSubscriptionCancelDto.from(requestSubscriptionCancelVo));

        return new BaseResponseEntity<>(BaseResponseStatus.SUCCESS);
    }
}
