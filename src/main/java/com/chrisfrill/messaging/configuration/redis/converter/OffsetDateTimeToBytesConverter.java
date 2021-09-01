package com.chrisfrill.messaging.configuration.redis.converter;

import com.chrisfrill.messaging.util.DateFormatter;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Log4j2
@Component
@WritingConverter
public class OffsetDateTimeToBytesConverter implements Converter<OffsetDateTime, byte[]> {
    @Override
    public byte[] convert(final OffsetDateTime offsetDateTime) {
        log.debug("Converting date to bytes: {}", offsetDateTime );
        return offsetDateTime.format(DateFormatter.formatter).getBytes();
    }
}