package back.vybz.paymentservice.subscription.batch.job;

import back.vybz.paymentservice.subscription.batch.dto.BillingDto;
import back.vybz.paymentservice.subscription.batch.processor.BillingPaymentProcessor;
import back.vybz.paymentservice.subscription.batch.reader.BillingPaymentReader;
import back.vybz.paymentservice.subscription.batch.writer.BillingPaymentWriter;
import back.vybz.paymentservice.subscription.domain.Subscription;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BillingBatchJob {

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final BillingPaymentReader billingPaymentReader;

    private final BillingPaymentProcessor billingPaymentProcessor;

    private final BillingPaymentWriter  billingPaymentWriter;

    @Bean
    public Job billingPaymentJob(){
        Step step = new StepBuilder("billingPaymentStep", jobRepository)
                .<Subscription, BillingDto>chunk(100, platformTransactionManager)
                .reader(billingPaymentReader)
                .processor(billingPaymentProcessor)
                .writer(billingPaymentWriter)
                .build();

        return new JobBuilder("billingPaymentJob", jobRepository)
                .start(step)
                .build();
    }
}
