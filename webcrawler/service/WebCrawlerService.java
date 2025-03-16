package com.webcrawler.service;


import com.webcrawler.dto.CrawlRequestDTO;
import com.webcrawler.dto.CrawlResponseDTO;

public interface WebCrawlerService {
    CrawlResponseDTO startCrawl(CrawlRequestDTO requestDTO);
    CrawlResponseDTO getCrawlResult(Long id, int page, int size);
}