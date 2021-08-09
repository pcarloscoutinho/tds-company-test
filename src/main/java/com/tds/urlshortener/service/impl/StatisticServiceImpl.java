package com.tds.urlshortener.service.impl;

import com.tds.urlshortener.dto.StatisticDTO;
import com.tds.urlshortener.dto.TopBrowserAccessDTO;
import com.tds.urlshortener.model.BrowserLog;
import com.tds.urlshortener.model.Url;
import com.tds.urlshortener.repository.BrowserLogRepository;
import com.tds.urlshortener.repository.UrlRepository;
import com.tds.urlshortener.service.StatisticService;
import com.tds.urlshortener.service.UrlService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticServiceImpl implements StatisticService {

    private final UrlService urlService;
    private final UrlRepository urlRepository;
    private final BrowserLogRepository browserLogRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public StatisticServiceImpl(UrlService urlService,
                                UrlRepository urlRepository,
                                BrowserLogRepository browserLogRepository,
                                ModelMapper modelMapper, MeterRegistry meterRegistry) {
        this.urlService = urlService;
        this.urlRepository = urlRepository;
        this.browserLogRepository = browserLogRepository;
        this.modelMapper = modelMapper;

        Counter.builder("short_url_access_count")
                .description("Short url access count.")
                .tags("shortUrl", "")
                .register(meterRegistry);
    }

    @Async
    @Override
    public Long incrementAccessCounter(Url url, String shortUrl) {
        Long accessCount = url.getStatistic().getTotalAccessCount();
        url.getStatistic().setTotalAccessCount(accessCount + 1);
        Metrics.counter("short_url_access_count", "shortUrl", shortUrl).increment();

        var savedUrl = urlRepository.save(url);

        return savedUrl.getStatistic().getTotalAccessCount();
    }

    @Override
    public StatisticDTO findStatisticByShortUrl(String shortUrl) {
        var url = urlService.findByShortUrl(shortUrl);
        var statisticDTO = modelMapper.map(url.getStatistic(), StatisticDTO.class);
        statisticDTO.setTopFiveBrowserAccess(findTopFiveBrowserAccess(url.getId()));

        return statisticDTO;
    }

    @Override
    public Url saveBrowserLog(Url url, Map<String, String> browserInfo) {
        String userAgent = browserInfo.get("User-Agent");
        url.getBrowserLogs().add(BrowserLog.builder().userAgent(userAgent).url(url).build());

        return urlRepository.save(url);
    }

    @Override
    public List<TopBrowserAccessDTO> findTopFiveBrowserAccess(Long urlId) {
        return browserLogRepository.findTopBrowserAccess(urlId)
                .orElse(List.of(new TopBrowserAccessDTO()))
                .stream()
                .limit(5)
                .collect(Collectors.toList());
    }
}
