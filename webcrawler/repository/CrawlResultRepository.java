package com.webcrawler.repository;

import com.webcrawler.entity.CrawlResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface CrawlResultRepository extends JpaRepository<CrawlResult, Long> {


    @Modifying
    @Transactional
    @Query("DELETE FROM CrawlResult c WHERE c.createdAt < :cutoffTime")
    void deleteOldCrawlResults(@Param("cutoffTime") LocalDateTime cutoffTime);

}
