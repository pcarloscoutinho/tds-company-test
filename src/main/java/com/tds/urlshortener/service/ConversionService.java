package com.tds.urlshortener.service;

public interface ConversionService {
    String encode(Long input);
    long decode(String input);
}
