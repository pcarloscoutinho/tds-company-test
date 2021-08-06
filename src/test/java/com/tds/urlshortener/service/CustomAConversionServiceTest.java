package com.tds.urlshortener.service;

import com.tds.urlshortener.service.impl.CustomAConversionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CustomAConversionServiceImpl.class})
class CustomAConversionServiceTest {

    @Autowired
    private ConversionService conversionService;

    @Test
    void encode() {
        String encode = conversionService.encode(1L);

        assertEquals("b", encode);
    }

    @Test
    void decode() {
        long decode = conversionService.decode("b");

        assertEquals(1L, decode);
    }
}