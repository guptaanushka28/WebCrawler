package com.webcrawler.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.webcrawler.entity.CrawlStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrawlResponseDTO {
    private Long id;
    private String seedUrl;
    private List<String> crawledUrls;
    private CrawlStatus status;
    private LocalDateTime createdAt;
    private int totalPages;
    private int totalUrls;

    public CrawlResponseDTO(Long id, String seedUrl, List<String> crawledUrls, CrawlStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.seedUrl = seedUrl;
        this.crawledUrls = crawledUrls;
        this.status = status;
        this.createdAt = createdAt;
    }


}
