package com.tds.urlshortener.service.impl;

import com.tds.urlshortener.service.ConversionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("customAConversionServiceImpl")
public class CustomAConversionServiceImpl implements ConversionService {

    private static final String ALLOWED_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final char[] ALLOWED_CHARACTERS = ALLOWED_STRING.toCharArray();
    private static final int BASE = ALLOWED_CHARACTERS.length;

    @Override
    public String encode(Long input) {
        var encodedString = new StringBuilder();

        if(input == 0) {
            return String.valueOf(ALLOWED_CHARACTERS[0]);
        }

        while (input > 0) {
            encodedString.append(ALLOWED_CHARACTERS[(int) (input % BASE)]);
            input = input / BASE;
        }

        return encodedString.reverse().toString();
    }

    @Override
    public long decode(String input) {
        var characters = input.toCharArray();
        var length = characters.length;

        var decoded = 0;

        //counter is used to avoid reversing input string
        var counter = 1;
        for (int i = 0; i < length; i++) {
            decoded += ALLOWED_STRING.indexOf(characters[i]) * Math.pow(BASE, length - counter);
            counter++;
        }
        return decoded;
    }
}
