package com.chrisfrill.messaging.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * Represents a message coming from a HTTP request
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class MessageRequest {
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ssZ")
    private OffsetDateTime timestamp;
}
