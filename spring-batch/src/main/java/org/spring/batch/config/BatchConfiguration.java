package org.spring.batch.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.spring.batch.repository.entity.Address;
import org.spring.batch.repository.entity.Coffee;
import org.spring.batch.service.CustomWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class BatchConfiguration {

    @Value("${file.input}")
    private String fileInput;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CustomWriter customWriter;

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
                .fieldSetMapper(fieldSet -> {
                    Coffee coffee = new Coffee();
                    coffee.setOrigin(fieldSet.readString("origin"));
                    coffee.setBrand(fieldSet.readString("brand"));
                    coffee.setCharacteristics(fieldSet.readString("characteristics"));
                    String addressJson = fieldSet.readString("address");
                    coffee.setAddress(getAddress(addressJson));
                    log.info("################# coffee:" + coffee.toString());
                    return coffee;
                })
                .build();
    }


    @Bean
    public ItemProcessor<Coffee,Coffee> processor() {
        return
            coffee ->
                new Coffee(
                        coffee.getBrand().toUpperCase(),
                        coffee.getOrigin().toUpperCase(),
                        coffee.getCharacteristics().toUpperCase(),
                        coffee.getAddress()
                );
    }

//    @Bean
//    public JdbcBatchItemWriter<Coffee> writer(DataSource dataSource) {
//        return new JdbcBatchItemWriterBuilder<Coffee>()
//            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
//            .sql("INSERT INTO coffee (brand, origin, characteristics, address) VALUES (:brand, :origin, :characteristics, :address)")
//            .dataSource(dataSource)
//            .build();
//    }


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
    public Step step1(JdbcBatchItemWriter<Coffee> writer, FlatFileItemReader<Coffee> reader) {
        return new StepBuilder("step1", jobRepository)
            .<Coffee, Coffee> chunk(10, transactionManager)
            .reader(reader)
            .processor(processor())
            .writer(customWriter)
            .listener(new ChunkExecutionListener())
            .build();
    }

    private  Address getAddress(String addressJson) {
        Address address = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            address = mapper.readValue(addressJson, Address.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing JSON address", e);
        }
        return address;
    }

}