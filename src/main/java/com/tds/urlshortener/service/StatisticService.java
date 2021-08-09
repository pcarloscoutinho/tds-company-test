package com.tds.urlshortener.service;

import com.tds.urlshortener.dto.StatisticDTO;
import com.tds.urlshortener.dto.TopBrowserAccessDTO;
import com.tds.urlshortener.model.Url;

import java.util.List;
import java.util.Map;

public interface StatisticService {
    Long incrementAccessCounter(Url url, String shortUrl);
    StatisticDTO findStatisticByShortUrl(String shortUrl);
    Url saveBrowserLog(Url url, Map<String, String> browserInfo);
    List<TopBrowserAccessDTO> findTopFiveBrowserAccess(Long urlId);
}
