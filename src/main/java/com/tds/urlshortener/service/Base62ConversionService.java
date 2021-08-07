package com.tds.urlshortener.service;

public interface Base62ConversionService {
    String encode(Long input);
    long decode(String input);
}
