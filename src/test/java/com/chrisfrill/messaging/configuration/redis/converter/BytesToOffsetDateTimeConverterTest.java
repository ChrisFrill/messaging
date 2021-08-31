package com.chrisfrill.messaging.configuration.redis.converter;

import com.chrisfrill.messaging.TestData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BytesToOffsetDateTimeConverterTest extends TestData {
    @Test
    public void convert_ReturnsCorrectDate_OnBytesProvided() {
        BytesToOffsetDateTimeConverter bytesToOffsetDateTimeConverter = new BytesToOffsetDateTimeConverter();
        assertEquals(bytesToOffsetDateTimeConverter.convert(firstDate.getBytes()), dateTime);
    }
}