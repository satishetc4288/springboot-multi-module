package org.spring.batch.config;

import lombok.extern.slf4j.Slf4j;
import org.spring.batch.repository.entity.Coffee;
import org.spring.batch.util.CommonUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.sql.PreparedStatement;

@Configuration
@Slf4j
public class BatchConfiguration {

    private static final String SQL = "INSERT INTO coffee (brand, origin, characteristics, address) VALUES (?,?,?,?);";

    @Value("${file.input}")
    private String fileInput;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private JobNotificationListener jobNotificationListener;

    @Bean
    @StepScope
    public FlatFileItemReader<Coffee> createReader() {
        return new FlatFileItemReaderBuilder<Coffee>()
            .name("coffeeItemReader")
            .resource(new ClassPathResource("coffee-list.csv"))
            .lineTokenizer(new DelimitedLineTokenizer() {{
                setDelimiter("||~~||");
                setNames("brand", "origin", "characteristics", "address");
            }})
            .fieldSetMapper(fieldSet ->
                Coffee.builder()
                    .brand(fieldSet.readString("brand"))
                    .origin(fieldSet.readString("origin"))
                    .characteristics(fieldSet.readString("characteristics"))
                    .address(CommonUtils.stringToObject(fieldSet.readString("address")))
                    .build())
            .build();
    }


    @Bean
    public ItemProcessor<Coffee,Coffee> processor() {
        return
    coffee ->
                Coffee.builder()
                    .brand(coffee.getBrand().toUpperCase())
                    .origin(coffee.getOrigin().toUpperCase())
                    .characteristics(coffee.getCharacteristics().toUpperCase())
                    .address(coffee.getAddress())
                    .build();
    }

    @Bean
    public ItemWriter<Coffee> write() {
        return list -> {
            PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(SQL);
            for (Coffee identity : list) {
                preparedStatement.setString(1, identity.getBrand());
                preparedStatement.setString(2, identity.getOrigin());
                preparedStatement.setString(3, identity.getCharacteristics());
                preparedStatement.setString(4, CommonUtils.objectToString(identity.getAddress()));
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        };
    }

    @Bean
    public Job importUserJob(Step step1) {
        return new JobBuilder("importUserJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .listener(jobNotificationListener)
            .flow(step1)
            .end()
            .build();
    }

    @Bean
    public Step step1() {
        return new StepBuilder("step1", jobRepository)
            .<Coffee, Coffee> chunk(10, transactionManager)
            .reader(createReader())
            .processor(processor())
            .writer(write())
            .listener(new ChunkExecutionListener())
            .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}