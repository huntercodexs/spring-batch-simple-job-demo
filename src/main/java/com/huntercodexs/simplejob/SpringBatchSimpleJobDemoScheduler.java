package com.huntercodexs.simplejob;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringBatchSimpleJobDemoScheduler {

    @Bean
    public JobDetail simpleJobDetail() {

        System.out.println("[DEBUG] >>> simpleJobDetail");

        return JobBuilder
            .newJob(SpringBatchSimpleJobDemoConfigQuartz.class)
            .storeDurably()
            .build();
    }

    @Bean
    public Trigger simpleJobTrigger() {

        System.out.println("[DEBUG] >>> simpleJobTrigger");

        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
            .dailyAtHourAndMinute(13, 46);

        return TriggerBuilder
            .newTrigger()
            .forJob(simpleJobDetail())
            .withSchedule(cronScheduleBuilder)
            .build();
    }
}
