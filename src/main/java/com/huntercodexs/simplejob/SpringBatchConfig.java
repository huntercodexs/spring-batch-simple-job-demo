package com.huntercodexs.simplejob;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowJobBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public JdbcCursorItemReader<ProductEntity> reader() {
        JdbcCursorItemReader<ProductEntity> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("SELECT * FROM PRODUCTS");
        reader.setRowMapper(new ProductResultRowMapper());
        return reader;
    }

    @Bean
    public ValidatingItemProcessor<ProductEntity> processor() {
        ValidatingItemProcessor<ProductEntity> processor = new ValidatingItemProcessor<>();
        processor.setValidator(new ProductValidator());
        processor.setFilter(true);
        return processor;
    }

    @Bean
    public FlatFileItemWriter<ProductEntity> writer() {
        FlatFileItemWriter<ProductEntity> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("~/csv/data.csv"));
        writer.setLineAggregator(getDelimitedLineAggregator());
        return writer;
    }

    private DelimitedLineAggregator<ProductEntity> getDelimitedLineAggregator() {
        BeanWrapperFieldExtractor<ProductEntity> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<ProductEntity>();
        beanWrapperFieldExtractor.setNames(new String[]{"id", "name", "description", "price"});

        DelimitedLineAggregator<ProductEntity> aggregator = new DelimitedLineAggregator<ProductEntity>();
        aggregator.setDelimiter(",");
        aggregator.setFieldExtractor(beanWrapperFieldExtractor);
        return aggregator;
    }

    @Bean
    public Step getDbToCsvStep() {
        StepBuilder stepBuilder = stepBuilderFactory.get("getDbToCsvStep");
        SimpleStepBuilder<ProductEntity, ProductEntity> simpleStepBuilder = stepBuilder.chunk(1);
        return simpleStepBuilder
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job dbToCsvJob() {
        JobBuilder jobBuilder = jobBuilderFactory.get("dbToCsvJob");
        jobBuilder.incrementer(new RunIdIncrementer());
        FlowJobBuilder flowJobBuilder = jobBuilder.flow(getDbToCsvStep()).end();
        Job job = flowJobBuilder.build();
        return job;
    }
}
