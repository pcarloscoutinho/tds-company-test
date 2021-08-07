package com.tds.urlshortener.service;

import com.tds.urlshortener.dto.CreateShortUrlDTO;
import com.tds.urlshortener.model.Url;
import com.tds.urlshortener.repository.UrlRepository;
import com.tds.urlshortener.service.impl.UrlServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.net.MalformedURLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UrlServiceImpl.class})
class UrlServiceTest {

    private static final String LONG_URL = "http://longurltest.com/longurltest";

    @MockBean
    private UrlRepository urlRepository;

    @MockBean
    @Qualifier("customABase62ConversionServiceImpl")
    private Base62ConversionService base62ConversionService;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private UrlService urlService;

    @Test
    void createShortUrl() throws MalformedURLException {
        CreateShortUrlDTO createShortUrlDTO = CreateShortUrlDTO.builder().longUrl(LONG_URL).build();

        when(modelMapper.map(createShortUrlDTO, Url.class)).thenReturn(Url.builder().longUrl(LONG_URL).build());
        when(urlRepository.save(any(Url.class))).thenReturn(Url.builder().id(1L).longUrl(LONG_URL).build());
        when(base62ConversionService.encode(1L)).thenReturn("b");

        String shortUrl = urlService.createShortUrl(createShortUrlDTO, "http://localhost:8081/api/v1/createUrl");
        String expectedResult = shortUrl.split("/")[5];

        verify(urlRepository, times(1)).save(isA(Url.class));
        assertEquals("b", expectedResult);
    }

    @Test
    void findByShortUrl() {
        when(base62ConversionService.decode("b")).thenReturn(1L);
        when(urlRepository.findById(1L)).thenReturn(Optional.of(Url.builder().id(1L).build()));

        Url url = urlService.findByShortUrl("b");

        assertNotNull(url);
    }

    @Test
    void findById() {
        when(urlRepository.findById(1L)).thenReturn(Optional.of(Url.builder().id(1L).build()));

        Url url = urlService.findById(1L);

        verify(urlRepository, times(1)).findById(1L);
        assertEquals(1L, url.getId());
    }

    @Test
    void findById_UrlDoesNotExist_ShouldThrowEntityNotFoundException() {
        when(urlRepository.findById(1L)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, ()-> urlService.findById(1L));
    }
}