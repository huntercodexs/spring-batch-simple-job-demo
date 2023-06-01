package com.huntercodexs.simplejob;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;
import java.time.LocalDateTime;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    @Value("${csv.filepath}")
    String csvFilepath;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean("getDbToCsv")
    public Step getDbToCsv() {

        System.out.println("[DEBUG] >>> getDbToCsv");

        return stepBuilderFactory
                .get("getDbToCsv")
                .<ProductEntity, ProductEntity>chunk(5_000)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<ProductEntity> reader() {

        System.out.println("[DEBUG] >>> reader");

        JdbcCursorItemReader<ProductEntity> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("SELECT * FROM PRODUCTS");
        reader.setRowMapper(new ProductResultRowMapper());
        return reader;
    }

    @Bean
    public ValidatingItemProcessor<ProductEntity> processor() {

        System.out.println("[DEBUG] >>> processor");

        ValidatingItemProcessor<ProductEntity> processor = new ValidatingItemProcessor<>();
        processor.setValidator(new ProductValidator());
        processor.setFilter(true);
        return processor;
    }

    @Bean
    public FlatFileItemWriter<ProductEntity> writer() {

        System.out.println("[DEBUG] >>> writer");

        FlatFileItemWriter<ProductEntity> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource(csvFilepath+"data-"+LocalDateTime.now()+".csv"));
        writer.setLineAggregator(getDelimitedLineAggregator());
        return writer;
    }

    private DelimitedLineAggregator<ProductEntity> getDelimitedLineAggregator() {

        System.out.println("[DEBUG] >>> getDelimitedLineAggregator");

        BeanWrapperFieldExtractor<ProductEntity> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<ProductEntity>();
        beanWrapperFieldExtractor.setNames(new String[]{"id", "name", "description", "price"});

        DelimitedLineAggregator<ProductEntity> aggregator = new DelimitedLineAggregator<ProductEntity>();
        aggregator.setDelimiter(";");
        aggregator.setFieldExtractor(beanWrapperFieldExtractor);
        return aggregator;
    }
}
