package com.huntercodexs.simplejob;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class SpringBatchSimpleJobDemoConfigJob {

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Bean
    public Job simpleJob(
        @Qualifier("getDbToCsv") Step getDbToCsvStep
    ) {

        System.out.println("[DEBUG] >>> simpleJob");

        return jobBuilderFactory
                .get("simpleJob")
                .start(getDbToCsvStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }
}
