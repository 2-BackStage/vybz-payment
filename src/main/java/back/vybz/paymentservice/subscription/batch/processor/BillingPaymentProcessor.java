package back.vybz.paymentservice.subscription.batch.processor;

import back.vybz.paymentservice.subscription.batch.dto.BillingDto;
import back.vybz.paymentservice.subscription.domain.Subscription;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@StepScope
@RequiredArgsConstructor
public class BillingPaymentProcessor implements ItemProcessor<Subscription, BillingDto>{

    @Override
    public BillingDto process(Subscription subscription) {
        return BillingDto.fromSubscription(subscription);
    }
}