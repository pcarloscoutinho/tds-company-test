package com.tds.urlshortener.service.impl;

import com.tds.urlshortener.model.Url;
import com.tds.urlshortener.service.MicrometerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@Qualifier("mockMicrometerServiceImpl")
public class MockMicrometerServiceImpl implements MicrometerService {

    @Override
    public void registerAccessCountGauge(String shortUrl, Url savedUrl) {
        log.info("Mock gauge build");
    }
}
