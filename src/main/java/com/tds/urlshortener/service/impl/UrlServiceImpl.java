package com.tds.urlshortener.service.impl;

import com.tds.urlshortener.dto.CreateShortUrlDTO;
import com.tds.urlshortener.model.Statistic;
import com.tds.urlshortener.model.Url;
import com.tds.urlshortener.repository.UrlRepository;
import com.tds.urlshortener.service.ConversionService;
import com.tds.urlshortener.service.UrlService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class UrlServiceImpl implements UrlService {

    private static final Long INITIAL_COUNT_VALUE = 0L;

    private final ConversionService conversionService;
    private final UrlRepository urlRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UrlServiceImpl(@Qualifier("customAConversionServiceImpl") ConversionService conversionService,
                          UrlRepository urlRepository,
                          ModelMapper modelMapper) {
        this.conversionService = conversionService;
        this.urlRepository = urlRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public String createShortUrl(CreateShortUrlDTO createShortUrlDTO, String requestUrl) {
        String formatUrl = null;

        var url = modelMapper.map(createShortUrlDTO, Url.class);
        url.setStatistic(Statistic.builder().totalAccessCount(INITIAL_COUNT_VALUE).build());

        var savedUrl = urlRepository.save(url);

        String encodedId = conversionService.encode(savedUrl.getId());

        try {
            var mountUrl = new URL(requestUrl);
            formatUrl = String.format("%s://%s:%d/api/v1/%s", mountUrl.getProtocol(), mountUrl.getHost(),
                    mountUrl.getPort(), encodedId);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return formatUrl;
    }

    @Override
    public Url findByShortUrl(String shortUrl) {
        Long urlId = conversionService.decode(shortUrl);
        return findById(urlId);
    }

    @Override
    public Url findById(Long id) {
        return urlRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Url not found with id: %d", id)));
    }
}
