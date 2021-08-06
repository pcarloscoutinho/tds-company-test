package com.tds.urlshortener.service;

import com.tds.urlshortener.dto.CreateShortUrlDTO;
import com.tds.urlshortener.model.Url;

public interface UrlService {
    String createShortUrl(CreateShortUrlDTO createShortUrlDTO, String requestUrl);
    Url findByShortUrl(String shortUrl);
    Url findById(Long id);
}
