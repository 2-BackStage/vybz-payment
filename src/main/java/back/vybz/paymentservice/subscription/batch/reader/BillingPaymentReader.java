package back.vybz.paymentservice.subscription.batch.reader;

import back.vybz.paymentservice.subscription.domain.Subscription;
import back.vybz.paymentservice.subscription.domain.SubscriptionStatus;
import back.vybz.paymentservice.subscription.infrastructure.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class BillingPaymentReader implements ItemReader<Subscription> {

    private final SubscriptionRepository subscriptionRepository;

    private Iterator<Subscription> subscriptionIterator;

    @Override
    public Subscription read() {
        if (subscriptionIterator == null) {
            LocalDate today = LocalDate.now();

            List<Subscription> subscriptions = subscriptionRepository
                    .findAllBySubscriptionStatusAndNextPaymentAtLessThanEqual(
                            SubscriptionStatus.ACTIVE,
                            LocalDateTime.now()
                    ).stream()
                    .filter(subscription ->
                            subscription.getFailCount() < 3 &&
                            !subscription.getNextPaymentAt().isAfter(LocalDateTime.now())
                    )
                    .toList();

            log.info("🗂️ 정기결제 대상 구독 수 : {} ", subscriptions);

            subscriptionIterator = subscriptions.iterator();
        }

        return subscriptionIterator.hasNext() ? subscriptionIterator.next() : null;
    }
}
