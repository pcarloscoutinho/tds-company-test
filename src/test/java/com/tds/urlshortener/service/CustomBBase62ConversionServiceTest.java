package com.tds.urlshortener.service;

import com.tds.urlshortener.service.impl.CustomBBase62ConversionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {CustomBBase62ConversionServiceImpl.class})
class CustomBBase62ConversionServiceTest {

    @Autowired
    private Base62ConversionService base62ConversionService;

    @Test
    void encode() {
        String encode = base62ConversionService.encode(1L);

        assertEquals("b", encode);
    }

    @Test
    void decode() {
        long decode = base62ConversionService.decode("b");

        assertEquals(1L, decode);
    }
}