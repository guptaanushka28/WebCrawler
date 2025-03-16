package com.webcrawler.scheduler;


import com.webcrawler.repository.CrawlResultRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CleanupTask  implements Job {

    private final CrawlResultRepository repository;

    @Autowired
    public CleanupTask(CrawlResultRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(JobExecutionContext context) {
        System.out.println("Cleanup job started at: " + LocalDateTime.now());
        repository.deleteOldCrawlResults(LocalDateTime.now().minusDays(30));
        System.out.println("Cleanup job finished.");
    }

}
