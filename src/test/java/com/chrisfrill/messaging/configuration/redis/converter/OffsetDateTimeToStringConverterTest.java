package com.chrisfrill.messaging.configuration.redis.converter;

import com.chrisfrill.messaging.TestData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OffsetDateTimeToStringConverterTest extends TestData {
    @Test
    public void convert_ReturnsCorrectDateInStringFormat_OnDateProvided() {
        OffsetDateTimeToStringConverter offsetDateTimeToStringConverter = new OffsetDateTimeToStringConverter();
        assertEquals(offsetDateTimeToStringConverter.convert(dateTime), date);
    }
}