package com.tds.urlshortener.service;

import com.tds.urlshortener.dto.StatisticDTO;
import com.tds.urlshortener.dto.TopBrowserAccessDTO;
import com.tds.urlshortener.model.BrowserLog;
import com.tds.urlshortener.model.Statistic;
import com.tds.urlshortener.model.Url;
import com.tds.urlshortener.repository.BrowserLogRepository;
import com.tds.urlshortener.repository.UrlRepository;
import com.tds.urlshortener.service.impl.StatisticServiceImpl;
import com.tds.urlshortener.service.impl.UrlServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {StatisticServiceImpl.class})
class StatisticServiceTest {

    @MockBean
    private UrlRepository urlRepository;

    @MockBean
    private UrlService urlService;

    @MockBean
    private BrowserLogRepository browserLogRepository;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private StatisticService statisticService;

    @Test
    void incrementAccessCounter() {
        Url initialUrl = Url.builder().id(1L)
                .statistic(Statistic.builder()
                        .totalAccessCount(2L).build())
                .build();

        Url incrementedCounterUrl = Url.builder().id(1L)
                .statistic(Statistic.builder()
                        .totalAccessCount(3L).build())
                .build();

        when(urlRepository.save(initialUrl)).thenReturn(incrementedCounterUrl);

        Long totalAccessCount = statisticService.incrementAccessCounter(initialUrl);

        verify(urlRepository, times(1)).save(isA(Url.class));
        assertEquals(3L, totalAccessCount);
    }

    @Test
    void findStatisticByShortUrl() {
        Url url = Url.builder().id(1L)
                .browserLogs(List.of(BrowserLog.builder().id(1L).userAgent("chrome").build()))
                .statistic(Statistic.builder().id(1L).totalAccessCount(1L).build())
                .build();

        when(urlService.findByShortUrl("b")).thenReturn(url);
        when(modelMapper.map(url.getStatistic(), StatisticDTO.class))
                .thenReturn(StatisticDTO.builder().totalAccessCount(url.getStatistic().getTotalAccessCount())
                        .build());
        when(browserLogRepository.findTopBrowserAccess(url.getId())).thenReturn(Optional.of(List.of(TopBrowserAccessDTO
                .builder().count(2L).userAgent("chrome").build())));

        StatisticDTO statisticDTO = statisticService.findStatisticByShortUrl("b");

        assertNotNull(statisticDTO);
    }

    @Test
    void saveBrowserLog() {
        Url url = Url.builder().id(1L)
                .browserLogs(new LinkedList<>(Collections.singletonList(BrowserLog.builder()
                        .userAgent("firefox").build())))
                .statistic(Statistic.builder().id(1L).totalAccessCount(1L).build())
                .build();

        when(urlRepository.save(url)).thenReturn(url);

        Url saveBrowserLog = statisticService.saveBrowserLog(url, Map.of("User-Agent", "chrome"));

        verify(urlRepository, times(1)).save(url);
        assertEquals(2, saveBrowserLog.getBrowserLogs().size());
        assertNotNull(saveBrowserLog);
    }

    @Test
    void findTopFiveBrowserAccess() {
        List<TopBrowserAccessDTO> browserLogs = new ArrayList<>();
        browserLogs.add(TopBrowserAccessDTO.builder().userAgent("chrome").count(15L).build());
        browserLogs.add(TopBrowserAccessDTO.builder().userAgent("internetExplorer").count(10L).build());
        browserLogs.add(TopBrowserAccessDTO.builder().userAgent("firefox").count(8L).build());
        browserLogs.add(TopBrowserAccessDTO.builder().userAgent("safari").count(5L).build());
        browserLogs.add(TopBrowserAccessDTO.builder().userAgent("opera").count(3L).build());
        browserLogs.add(TopBrowserAccessDTO.builder().userAgent("edge").count(1L).build());

        when(browserLogRepository.findTopBrowserAccess(1L))
                .thenReturn(Optional.of(browserLogs));

        List<TopBrowserAccessDTO> topFiveBrowserAccess = statisticService.findTopFiveBrowserAccess(1L);

        assertEquals(5, topFiveBrowserAccess.size());
    }
}