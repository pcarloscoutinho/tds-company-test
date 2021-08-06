package com.tds.urlshortener.repository;

import com.tds.urlshortener.dto.TopBrowserAccessDTO;
import com.tds.urlshortener.model.BrowserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BrowserLogRepository extends JpaRepository<BrowserLog, Long> {

    @Query("SELECT new com.tds.urlshortener.dto.TopBrowserAccessDTO(bl.userAgent, COUNT(*)) " +
            "FROM BrowserLog bl " +
            "WHERE bl.url.id = :urlId " +
            "GROUP BY bl.userAgent " +
            "ORDER BY count(*) DESC")
    Optional<List<TopBrowserAccessDTO>> findTopBrowserAccess(Long urlId);
}
