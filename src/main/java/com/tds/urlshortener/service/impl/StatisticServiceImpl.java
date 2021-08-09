package com.tds.urlshortener.service.impl;

import com.tds.urlshortener.dto.StatisticDTO;
import com.tds.urlshortener.dto.TopBrowserAccessDTO;
import com.tds.urlshortener.model.BrowserLog;
import com.tds.urlshortener.model.Url;
import com.tds.urlshortener.repository.BrowserLogRepository;
import com.tds.urlshortener.repository.UrlRepository;
import com.tds.urlshortener.service.StatisticService;
import com.tds.urlshortener.service.UrlService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class StatisticServiceImpl implements StatisticService {

    private final UrlService urlService;
    private final UrlRepository urlRepository;
    private final BrowserLogRepository browserLogRepository;
    private final ModelMapper modelMapper;
    private final MeterRegistry meterRegistry;
    private final Map<String, AtomicLong> currentAccessCount = new HashMap<>();

    @Autowired
    public StatisticServiceImpl(UrlService urlService,
                                UrlRepository urlRepository,
                                BrowserLogRepository browserLogRepository,
                                ModelMapper modelMapper, MeterRegistry meterRegistry) {
        this.urlService = urlService;
        this.urlRepository = urlRepository;
        this.browserLogRepository = browserLogRepository;
        this.modelMapper = modelMapper;
        this.meterRegistry = meterRegistry;
    }

    @Async
    @Override
    public Long incrementAccessCounter(Url url, String shortUrl) {
        Long accessCount = url.getStatistic().getTotalAccessCount();
        url.getStatistic().setTotalAccessCount(accessCount + 1);

        currentAccessCount.computeIfAbsent(shortUrl, v -> new AtomicLong());

        var savedUrl = urlRepository.save(url);

        Objects.requireNonNull(this.meterRegistry.gauge("short_url_access_count",
                Tags.of("shortUrl", shortUrl), currentAccessCount.get(shortUrl)))
                .set(savedUrl.getStatistic().getTotalAccessCount());

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
