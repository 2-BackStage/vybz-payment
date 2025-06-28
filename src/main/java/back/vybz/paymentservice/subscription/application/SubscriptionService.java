package back.vybz.paymentservice.subscription.application;

import back.vybz.paymentservice.subscription.dto.request.RequestSubscriptionCancelDto;
import back.vybz.paymentservice.subscription.dto.request.RequestSubscriptionCreateDto;
import back.vybz.paymentservice.subscription.dto.request.RequestSubscriptionExecuteDto;
import back.vybz.paymentservice.subscription.dto.response.ResponseBillingKeyDto;

public interface SubscriptionService {

    ResponseBillingKeyDto registerBillingKey(RequestSubscriptionCreateDto requestSubscriptionCreateDto);

    void executeBillingPayment(RequestSubscriptionExecuteDto requestSubscriptionExecuteDto);

    void cancelSubscription(RequestSubscriptionCancelDto requestSubscriptionCancelDto);
}
