package back.vybz.paymentservice.subscription.batch.scheduler;

import back.vybz.paymentservice.common.entity.BaseResponseStatus;
import back.vybz.paymentservice.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BillingPaymentBatchScheduler {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @Scheduled(cron = "0 0 11 * * *")
    public void runBillingJob() {
        try {

            log.info("📌 BillingPaymentJob 스케줄러 실행 시작");

            Job job = jobRegistry.getJob("billingPaymentJob");
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(job, jobParameters);

            log.info("✅ BillingPaymentJob 정상 실행 완료");

        } catch (Exception e) {

            log.error("❌ BillingPaymentJob 실행 실패", e);

            throw new BaseException(BaseResponseStatus.BILLING_BATCH_FAIL);
        }
    }
}
