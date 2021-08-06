package com.tds.urlshortener.controller;

import com.tds.urlshortener.dto.StatisticDTO;
import com.tds.urlshortener.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/statistics")
public class StatisticController {

    private final StatisticService statisticService;

    @Autowired
    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<StatisticDTO> getStatisticsByShortUrl(@PathVariable String shortUrl) {
        StatisticDTO statistics = statisticService.findStatisticByShortUrl(shortUrl);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(statistics);
    }
}
