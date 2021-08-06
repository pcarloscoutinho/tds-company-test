package com.tds.urlshortener.service.impl;

import com.tds.urlshortener.service.ConversionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Qualifier("customBConversionServiceImpl")
public class CustomBConversionServiceImpl implements ConversionService {

    Map<Long, String> indexToUrl = new HashMap<>();
    String base62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Override
    public String encode(Long input) {
        StringBuilder sb = new StringBuilder();
        while (input != 0) {
            sb.append(base62.charAt((int)(input % 62)));
            input /= 62;
        }
        while (sb.length() < 6) {
            sb.append(0);
        }
        return sb.reverse().toString();
    }

    @Override
    public long decode(String input) {
        String base62Encoded = input.substring(input.lastIndexOf("/") + 1);
        long decode = 0;
        for(int i = 0; i < base62Encoded.length(); i++) {
            decode = decode * 62 + base62.indexOf("" + base62Encoded.charAt(i));
        }
        return Long.parseLong(indexToUrl.get(decode));
    }
}
