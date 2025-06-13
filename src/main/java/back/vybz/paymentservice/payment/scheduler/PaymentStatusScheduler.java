package back.vybz.paymentservice.payment.scheduler;

import back.vybz.paymentservice.payment.domain.Payment;
import back.vybz.paymentservice.payment.domain.PaymentStatus;
import back.vybz.paymentservice.payment.infrastructure.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentStatusScheduler {

    private final PaymentRepository paymentRepository;

    @Transactional
    @Scheduled(cron = "0 0/10 * * * *")
    public void statusAbortPayment() {

        LocalDateTime threshold = LocalDateTime.now().minusHours(1);
        List<Payment> expiredPayments = paymentRepository.findByPaymentStatusAndCreatedAtBefore(PaymentStatus.READY, threshold);

        if (expiredPayments.isEmpty()) {
            log.info("[PaymentScheduler] 만료된 결제 없음");
            return;
        }

        expiredPayments.forEach(Payment::markAborted);
        paymentRepository.saveAll(expiredPayments);

        log.info("[PaymentScheduler] {}건의 결제를 ABORTED 처리함", expiredPayments.size());
    }
}
