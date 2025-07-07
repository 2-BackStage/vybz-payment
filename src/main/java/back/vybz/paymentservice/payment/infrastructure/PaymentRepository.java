package back.vybz.paymentservice.payment.infrastructure;

import back.vybz.paymentservice.payment.domain.Payment;
import back.vybz.paymentservice.payment.domain.PaymentStatus;
import back.vybz.paymentservice.payment.domain.PaymentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(String orderId);

    List<Payment> findByPaymentStatusAndCreatedAtBefore(PaymentStatus paymentStatus, LocalDateTime localDateTime);

    Optional<Payment> findByPaymentKey(String paymentKey);

    Page<Payment> findByUserUuidAndPaymentStatusAndPaymentTypeAndApprovedAtIsNotNull(String userUuid, PaymentStatus paymentStatus, PaymentType paymentType, Pageable pageable);

    boolean existsByUserUuid(String userUuid);
}
