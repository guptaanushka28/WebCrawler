package com.webcrawler.service.impl;


import com.webcrawler.dto.CrawlRequestDTO;
import com.webcrawler.dto.CrawlResponseDTO;
import com.webcrawler.entity.CrawlResult;
import com.webcrawler.entity.CrawlStatus;
import com.webcrawler.repository.CrawlResultRepository;
import com.webcrawler.service.WebCrawlerService;
import jakarta.persistence.EntityManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class WebCrawlerServiceImpl implements WebCrawlerService {

    private static final int MAX_THREADS = 5;

    @Autowired
    private CrawlResultRepository crawlResultRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    @Qualifier("crawlerExecutor")
    private ThreadPoolTaskExecutor executor;

    @Override
    @Transactional
    public CrawlResponseDTO startCrawl(CrawlRequestDTO requestDTO) {
        if (requestDTO.getSeedUrl() == null || requestDTO.getSeedUrl().trim().isEmpty()) {
            throw new IllegalArgumentException("Seed URL cannot be empty or null");
        }

        CrawlResult crawlResult = new CrawlResult();
        crawlResult.setSeedUrl(requestDTO.getSeedUrl());
        crawlResult.setStatus(CrawlStatus.IN_PROGRESS);

        CrawlResult savedResult = crawlResultRepository.saveAndFlush(crawlResult);
        entityManager.flush();
        entityManager.clear();

        System.out.println("Saved CrawlResult ID: " + savedResult.getId());
        System.out.println("Saved Seed URL in DB: " + savedResult.getSeedUrl());

        CompletableFuture.runAsync(() -> {
            try {
                crawlWebsite(savedResult);
            } catch (InterruptedException e) {
                throw new RuntimeException("Crawling was interrupted", e);
            }
        }, executor);
        System.out.println("Saved Result: " + savedResult);


        return new CrawlResponseDTO(savedResult.getId(), savedResult.getSeedUrl(),  savedResult.getCrawledUrls(), savedResult.getStatus(), savedResult.getCreatedAt());

    }


    @Async("crawlerExecutor")
    public CompletableFuture<Void> crawlWebsite(CrawlResult crawlResult) throws InterruptedException {
        System.out.println("Starting async crawl for ID: " + crawlResult.getId());
        Thread.sleep(100);
        Optional<CrawlResult> resultOpt = crawlResultRepository.findById(crawlResult.getId());
        if (resultOpt.isEmpty()) {
            System.err.println("CrawlResult not found in DB for ID: " + crawlResult.getId());
            return CompletableFuture.completedFuture(null);
        }

        CrawlResult refreshedResult = resultOpt.get();
        System.out.println("Crawling URL: " + refreshedResult.getSeedUrl());

        try {
            Document doc = Jsoup.connect(refreshedResult.getSeedUrl())
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                    .timeout(10000)
                    .get();

            Elements links = doc.select("a[href]");
            List<String> crawledUrls = links.stream()
                    .map(link -> link.attr("abs:href"))
                    .filter(url -> url.length() <= 2048)
                    .distinct()
                    .collect(Collectors.toList());

            refreshedResult.setCrawledUrls(crawledUrls);
            refreshedResult.setStatus(CrawlStatus.COMPLETED);
        } catch (Exception e) {
            System.err.println("Error during crawling: " + e.getMessage());
            refreshedResult.setStatus(CrawlStatus.FAILED);
        }

        crawlResultRepository.save(refreshedResult);

        return CompletableFuture.completedFuture(null);
    }


    public CrawlResponseDTO getCrawlResult(Long id, int page, int size) {
        CrawlResult result = crawlResultRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Crawl result not found with ID: " + id));

        List<String> crawledUrls = result.getCrawledUrls();

        int totalUrls = (crawledUrls != null) ? crawledUrls.size() : 0;
        int totalPages = (totalUrls > 0) ? (int) Math.ceil((double) totalUrls / size) : 1;

        int start = Math.min(page * size, totalUrls);
        int end = Math.min(start + size, totalUrls);
        List<String> paginatedUrls = (totalUrls > 0) ? crawledUrls.subList(start, end) : new ArrayList<>();

        return new CrawlResponseDTO(
                result.getId(),
                result.getSeedUrl(),
                paginatedUrls,
                result.getStatus(),
                result.getCreatedAt(),
                totalPages,
                totalUrls
        );
    }
}
