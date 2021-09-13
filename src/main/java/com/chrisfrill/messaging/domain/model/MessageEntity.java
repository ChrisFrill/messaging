package com.chrisfrill.messaging.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * Represents a message persisted in the underlying persistent storage
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class MessageEntity {
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ssZ")
    private OffsetDateTime timestamp;
}
