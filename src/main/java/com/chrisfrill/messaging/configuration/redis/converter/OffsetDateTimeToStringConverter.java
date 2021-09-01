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
public class OffsetDateTimeToStringConverter implements Converter<OffsetDateTime, String> {
    /**
     * Converts date to String with yyyy-MM-dd HH:mm:ssxx format
     *
     * @return A String representation of the provided date in yyyy-MM-dd HH:mm:ssxx format
     */
    @Override
    public String convert(final OffsetDateTime offsetDateTime) {
        log.debug("Converting date to String: {}", offsetDateTime );
        return offsetDateTime.format(DateFormatter.formatter);
    }
}