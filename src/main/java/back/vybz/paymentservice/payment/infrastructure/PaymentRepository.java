package back.vybz.paymentservice.payment.infrastructure;

import back.vybz.paymentservice.payment.domain.Payment;
import back.vybz.paymentservice.payment.domain.PaymentStatus;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(String orderId);

    List<Payment> findByPaymentStatusAndCreatedAtBefore(PaymentStatus paymentStatus, LocalDateTime localDateTime);

    Optional<Payment> findByPaymentKey(String paymentKey);

    Page<Payment> findByUserUuidAndPaymentStatusAndApprovedAtIsNotNull(String userUuid, PaymentStatus paymentStatus, Pageable pageable);

    boolean existsByUserUuid(String userUuid);
}
