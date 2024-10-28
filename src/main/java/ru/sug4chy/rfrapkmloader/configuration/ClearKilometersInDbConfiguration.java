package ru.sug4chy.rfrapkmloader.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ClearKilometersInDbConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ClearKilometersInDbConfiguration.class);

    @Bean
    public Tasklet clearKilometersInDbTasklet(JdbcTemplate jdbcTemplate) {
        return (stepContribution, chunkContext) -> {
            logger.info("Started clearing kilometer poles in DB");
            jdbcTemplate.update("DELETE FROM verified_point WHERE \"Discriminator\" = 'KilometerPole'");
            logger.info("Finished clearing kilometer poles in DB");

            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Step clearKilometersInDbStep(JobRepository jobRepository,
                                        PlatformTransactionManager transactionManager,
                                        JdbcTemplate jdbcTemplate
    ) {
        return new StepBuilder("clearKilometersInDbStep", jobRepository)
                .tasklet(clearKilometersInDbTasklet(jdbcTemplate), transactionManager)
                .build();
    }
}
