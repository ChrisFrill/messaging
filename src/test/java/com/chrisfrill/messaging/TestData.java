package com.chrisfrill.messaging;

import com.chrisfrill.messaging.domain.model.MessageEntity;
import com.chrisfrill.messaging.util.DateFormatter;

import java.time.OffsetDateTime;

public class TestData {
    public String date = "2018-10-09 00:12:12+0000";

    public OffsetDateTime dateTime = OffsetDateTime.parse(date, DateFormatter.formatter);

    String palindrome = "aba";
    public final MessageEntity messageEntity = new MessageEntity("1", palindrome, dateTime);
}
