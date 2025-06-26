package back.vybz.paymentservice.subscription.infrastructure;

import back.vybz.paymentservice.subscription.domain.Subscription;
import back.vybz.paymentservice.subscription.domain.SubscriptionStatus;
import back.vybz.paymentservice.subscription.dto.request.RequestSubscriptionCancelDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByCustomerKey(String customerKey);

    List<Subscription> findAllBySubscriptionStatusAndNextPaymentAtLessThanEqual(
            SubscriptionStatus subscriptionStatus, LocalDateTime now);

    Optional<Subscription> findByUserUuidAndBuskerUuid(String userUuid, String buskerUuid);

    Optional<Subscription> findByUserUuidAndBuskerUuidAndDeletedFalse(String userUuid, String buskerUuid);
}
