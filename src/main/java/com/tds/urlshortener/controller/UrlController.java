package com.tds.urlshortener.controller;

import com.tds.urlshortener.dto.CreateShortUrlDTO;
import com.tds.urlshortener.dto.ShortUrlResponseDTO;
import com.tds.urlshortener.service.StatisticService;
import com.tds.urlshortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class UrlController {

    private final UrlService urlService;
    private final StatisticService statisticService;
    private final HttpServletRequest httpServletRequest;

    @Autowired
    public UrlController(UrlService urlService, StatisticService statisticService,
                         HttpServletRequest httpServletRequest) {
        this.urlService = urlService;
        this.statisticService = statisticService;
        this.httpServletRequest = httpServletRequest;
    }

    @PostMapping("/url/createUrl")
    public ResponseEntity<ShortUrlResponseDTO> createUrl(@RequestBody CreateShortUrlDTO createShortUrlDTO) {
        String shortUrl = urlService.createShortUrl(createShortUrlDTO, httpServletRequest.getRequestURL().toString());
        ShortUrlResponseDTO shortUrlResponseDTO = ShortUrlResponseDTO.builder().shortUrl(shortUrl).build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(shortUrlResponseDTO);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortUrl) {
        Map<String, String> browserInfo = new HashMap<>();
        browserInfo.put("User-Agent", httpServletRequest.getHeader("User-Agent"));

        var url = urlService.findByShortUrl(shortUrl);
        String longUrl = url.getLongUrl();

        statisticService.saveBrowserLog(url, browserInfo);
        statisticService.incrementAccessCounter(url);

        return ResponseEntity
                .status(HttpStatus.PERMANENT_REDIRECT)
                .location(URI.create(longUrl))
                .build();
    }
}
