package back.vybz.paymentservice.payment.infrastructure;

import back.vybz.paymentservice.payment.domain.RefundHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundHistoryRepository extends JpaRepository<RefundHistory, Long> {

    boolean existsByPaymentKey(String paymentKey);
}


