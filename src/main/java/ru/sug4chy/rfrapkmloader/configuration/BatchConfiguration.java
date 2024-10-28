package ru.sug4chy.rfrapkmloader.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import ru.sug4chy.rfrapkmloader.entity.VerifiedPoint;
import ru.sug4chy.rfrapkmloader.model.xml.PlacemarkXmlModel;
import ru.sug4chy.rfrapkmloader.spring.batch.listener.JobCompletionNotificationListener;
import ru.sug4chy.rfrapkmloader.spring.batch.processor.PlacemarkToVerifiedPointItemProcessor;

import javax.sql.DataSource;

@Configuration
public class BatchConfiguration {

    @Bean
    public JdbcBatchItemWriter<VerifiedPoint> jdbcBatchItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<VerifiedPoint>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO verified_point(id, road_id, point_name, point_type, coordinates_latitude, " +
                        "coordinates_longitude, \"Discriminator\", region_id, description, created_at_utc, " +
                        "lastly_updated_at_utc) " +
                        "VALUES (:id, :road.id, :pointName, :pointType, :latitude, :longitude, :discriminator, " +
                        ":region.id, :description, :createdAtUtc, :lastlyUpdatedAtUtc)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Step movePointsFromKmlToDbStep(JobRepository jobRepository,
                                          StaxEventItemReader<PlacemarkXmlModel> reader,
                                          PlacemarkToVerifiedPointItemProcessor processor,
                                          JdbcBatchItemWriter<VerifiedPoint> writer,
                                          PlatformTransactionManager transactionManager) {
        return new StepBuilder("movePointsFromKmlToDbStep", jobRepository)
                .<PlacemarkXmlModel, VerifiedPoint>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job writeKilometersFromKmlToDbJob(JobRepository jobRepository,
                                             JobCompletionNotificationListener listener,
                                             Step clearKilometersInDbStep,
                                             Step movePointsFromKmlToDbStep) {
        return new JobBuilder("writeKilometersFromKmlToDbJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(clearKilometersInDbStep)
                .next(movePointsFromKmlToDbStep)
                .build();
    }
}