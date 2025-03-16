package com.webcrawler.config;

import com.webcrawler.scheduler.CleanupTask;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail cleanupJobDetail() {
        return JobBuilder.newJob(CleanupTask.class)
                .withIdentity("cleanupJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger cleanupJobTrigger(JobDetail cleanupJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(cleanupJobDetail)
                .withIdentity("cleanupTrigger")
                // Runs daily at midnight
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?"))
                .build();
    }
    @Bean
    public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.start();
        return scheduler;
    }
}
