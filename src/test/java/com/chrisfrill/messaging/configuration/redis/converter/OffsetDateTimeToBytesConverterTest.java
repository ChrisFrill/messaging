package com.chrisfrill.messaging.configuration.redis.converter;

import com.chrisfrill.messaging.TestData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OffsetDateTimeToBytesConverterTest extends TestData {
    @Test
    public void convert_ReturnsCorrectDateInBytes_OnDateProvided() {
        OffsetDateTimeToBytesConverter offsetDateTimeToBytesConverter = new OffsetDateTimeToBytesConverter();
        assertArrayEquals(offsetDateTimeToBytesConverter.convert(dateTime), date.getBytes());
    }
}