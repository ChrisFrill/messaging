package com.chrisfrill.messaging.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
public class MessageResponse {
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ssZ")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ssZ")
    private OffsetDateTime timestamp;

    @JsonIgnore
    @JsonProperty(value = "longest_palindrome_size", access = JsonProperty.Access.READ_ONLY)
    private Integer longestPalindromeSize;

    public MessageResponse(String content, OffsetDateTime timestamp) {
        this.content = content;
        this.timestamp = timestamp;
    }
}
