package ru.sug4chy.rfrapkmloader.spring.batch.listener;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.sug4chy.rfrapkmloader.repository.VerifiedPointRepository;

@Component
@RequiredArgsConstructor
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final VerifiedPointRepository verifiedPointRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void afterJob(@NotNull JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            LOGGER.info("Job finished. Result: {}", verifiedPointRepository.countByPointType("KilometerPole"));
        }
    }

    @PreDestroy
    public void preDestroy() {
        jdbcTemplate.update("DROP TABLE IF EXISTS batch_job_execution CASCADE;");
        jdbcTemplate.update("DROP TABLE IF EXISTS batch_job_execution_context CASCADE;");
        jdbcTemplate.update("DROP TABLE IF EXISTS batch_job_execution_params CASCADE;");
        jdbcTemplate.update("DROP TABLE IF EXISTS batch_job_instance CASCADE;");
        jdbcTemplate.update("DROP TABLE IF EXISTS batch_step_execution CASCADE;");
        jdbcTemplate.update("DROP TABLE IF EXISTS batch_step_execution_context CASCADE;");
    }
}