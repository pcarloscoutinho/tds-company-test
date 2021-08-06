package com.tds.urlshortener.service.impl;

import com.tds.urlshortener.service.ConversionService;
import org.apache.tomcat.util.codec.binary.BaseNCodec;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.unbrokendome.base62.Base62;

@Service
@Qualifier("libConversionServiceImpl")
public class LibConversionServiceImpl implements ConversionService {

//    private final Base62 base62;

//    @Autowired
//    public LibConversionServiceImpl(Base62 base62) {
//        this.base62 = base62;
//    }

    @Override
    public String encode(Long input) {
        return Base62.encode(input);
//        return new String(base62.encode(Longs.toByteArray(input)));
    }

    @Override
    public long decode(String input) {
        return 1L;
    }
}
