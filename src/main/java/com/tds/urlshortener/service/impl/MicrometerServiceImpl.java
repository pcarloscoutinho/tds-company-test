package com.tds.urlshortener.service.impl;

import com.tds.urlshortener.model.Url;
import com.tds.urlshortener.service.MicrometerService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Primary
@Qualifier("micrometerServiceImpl")
public class MicrometerServiceImpl implements MicrometerService {

    private final Map<String, AtomicLong> currentAccessCount = new HashMap<>();
    private final MeterRegistry meterRegistry;

    @Autowired
    public MicrometerServiceImpl(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void registerAccessCountGauge(String shortUrl, Url savedUrl) {
        currentAccessCount.computeIfAbsent(shortUrl, v -> new AtomicLong());
        Objects.requireNonNull(this.meterRegistry.gauge("short_url_access_count",
                Tags.of("shortUrl", shortUrl), currentAccessCount.get(shortUrl)))
                .set(savedUrl.getStatistic().getTotalAccessCount());
    }
}
