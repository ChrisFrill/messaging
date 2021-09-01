package com.chrisfrill.messaging.configuration.redis.converter;

import com.chrisfrill.messaging.util.DateFormatter;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Log4j2
@Component
@ReadingConverter
public class BytesToOffsetDateTimeConverter implements Converter<byte[], OffsetDateTime> {
    @Override
    public OffsetDateTime convert(final byte[] source) {
        log.debug("Converting bytes to date: {}", source );
        return OffsetDateTime.parse(new String(source), DateFormatter.formatter);
    }
}