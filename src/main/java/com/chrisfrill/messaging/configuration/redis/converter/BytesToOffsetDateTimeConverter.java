package com.chrisfrill.messaging.configuration.redis.converter;

import com.chrisfrill.messaging.util.DateFormatter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
@ReadingConverter
public class BytesToOffsetDateTimeConverter implements Converter<byte[], OffsetDateTime> {
    @Override
    public OffsetDateTime convert(final byte[] source) {
        return OffsetDateTime.parse(new String(source), DateFormatter.formatter);
    }
}