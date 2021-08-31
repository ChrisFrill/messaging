package com.chrisfrill.messaging;

import com.chrisfrill.messaging.util.DateFormatter;

import java.time.OffsetDateTime;

public class TestData {
    String date = "2018-10-09 00:12:12+0000";

    public OffsetDateTime dateTime = OffsetDateTime.parse(date, DateFormatter.formatter);
}
