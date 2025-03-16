package com.webcrawler.controller;

import com.webcrawler.dto.CrawlRequestDTO;
import com.webcrawler.dto.CrawlResponseDTO;
import com.webcrawler.endpoint.CrawlControllerEndpoint;
import com.webcrawler.service.WebCrawlerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crawl")
@Tag(name = "Web Crawler API", description = "APIs for managing web crawling")
public class CrawlController implements CrawlControllerEndpoint {

    private final WebCrawlerService crawlService;

    public CrawlController(WebCrawlerService crawlService) {
        this.crawlService = crawlService;
    }

    @Override
    public ResponseEntity<CrawlResponseDTO> startCrawl(CrawlRequestDTO crawlRequestDTO) {
        return ResponseEntity.ok(crawlService.startCrawl(crawlRequestDTO));
    }

    @Override
    public ResponseEntity<CrawlResponseDTO> getCrawlResult(Long id, int page, int size) {
        return ResponseEntity.ok(crawlService.getCrawlResult(id, page, size));
    }
}
