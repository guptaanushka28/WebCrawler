package com.webcrawler.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CrawlResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "SEED_URL", nullable = false)
    private String seedUrl;

    @Column(name = "CRAWLED_URLS", length = 2048)
    @ElementCollection
    private List<String> crawledUrls = new ArrayList<>();

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private CrawlStatus status;

    @CreationTimestamp
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

}

