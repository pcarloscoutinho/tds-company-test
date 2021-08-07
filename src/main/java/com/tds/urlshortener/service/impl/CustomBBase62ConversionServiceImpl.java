package com.tds.urlshortener.service.impl;

import com.tds.urlshortener.service.Base62ConversionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Qualifier("customBBase62ConversionServiceImpl")
public class CustomBBase62ConversionServiceImpl implements Base62ConversionService {

    private static final char[] DICTIONARY_62 =
            new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    @Override
    public String encode(Long input) {
        List<Character> result = new ArrayList<>();
        var base = new BigInteger("" + DICTIONARY_62.length);
        var exponent = 1;
        var remaining = BigInteger.valueOf(input);
        while (true) {
            BigInteger a = base.pow(exponent); //16^1 = 16
            BigInteger b = remaining.mod(a); //119 % 16 = 7 | 112 % 256 = 112
            BigInteger c = base.pow(exponent - 1);
            BigInteger d = b.divide(c);

            //if d > dictionary.length, we have a problem. but BigInteger doesnt have
            //a greater than method :-(  hope for the best. theoretically, d is always
            //an index of the dictionary!
            result.add(DICTIONARY_62[d.intValue()]);
            remaining = remaining.subtract(b); //119 - 7 = 112 | 112 - 112 = 0

            //finished?
            if (remaining.equals(BigInteger.ZERO)) {
                break;
            }

            exponent++;
        }

        //need to reverse it, since the start of the list contains the least significant values
        var stringBuffer = new StringBuffer();
        for (int i = result.size() - 1; i >= 0; i--) {
            stringBuffer.append(result.get(i));
        }
        return stringBuffer.toString();
    }

    @Override
    public long decode(String input) {
        //reverse it, coz its already reversed!
        var chars = new char[input.length()];
        input.getChars(0, input.length(), chars, 0);

        var chars2 = new char[input.length()];
        int i = chars2.length - 1;
        for (char c : chars) {
            chars2[i--] = c;
        }

        //for efficiency, make a map
        Map<Character, BigInteger> dictMap = new HashMap<Character, BigInteger>();
        var j = 0;
        for (char c : DICTIONARY_62) {
            dictMap.put(c, new BigInteger("" + j++));
        }

        BigInteger bi = BigInteger.ZERO;
        var base = new BigInteger("" + DICTIONARY_62.length);
        var exponent = 0;
        for (char c : chars2) {
            BigInteger a = dictMap.get(c);
            BigInteger b = base.pow(exponent).multiply(a);
            bi = bi.add(new BigInteger("" + b));
            exponent++;
        }

        return bi.longValue();

    }
}

