package com.chrisfrill.messaging;

import com.chrisfrill.messaging.domain.model.MessageEntity;
import com.chrisfrill.messaging.domain.model.dto.MessageRequest;
import com.chrisfrill.messaging.domain.model.dto.MessageResponse;
import com.chrisfrill.messaging.util.DateFormatter;

import java.time.OffsetDateTime;
import java.time.ZoneId;

public class TestData {
    public String firstDate = "2018-10-09 00:12:12+0000";
    String secondDate = "2018-10-09 00:13:12+0100";


    public OffsetDateTime dateTime = OffsetDateTime.parse(firstDate, DateFormatter.formatter);
    public OffsetDateTime dateTime2 = OffsetDateTime.parse(secondDate, DateFormatter.formatter.withZone(ZoneId.of("+01:00")));

    String palindrome = "aba";
    String notPalindrome = "1";
    public final MessageEntity messageEntity = new MessageEntity(palindrome, dateTime);
    public final MessageResponse messageResponse = new MessageResponse(palindrome, dateTime);
    public final MessageRequest messageRequest = new MessageRequest(palindrome, dateTime);
    public final MessageRequest invalidMessageRequest = new MessageRequest(null, dateTime);

    public final MessageEntity secondMessageEntity = new MessageEntity(notPalindrome, dateTime2);
}
