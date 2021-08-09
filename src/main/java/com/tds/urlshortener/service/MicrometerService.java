package com.tds.urlshortener.service;

import com.tds.urlshortener.model.Url;

public interface MicrometerService {
    void registerAccessCountGauge(String shortUrl, Url savedUrl);
}
