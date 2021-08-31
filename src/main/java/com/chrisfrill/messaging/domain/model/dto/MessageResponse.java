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

    public Integer getLongestPalindromeSize() {
        if (content == null) {
            throw new RuntimeException("Message content should be provided");
        }
        String alphabeticContent = content.replaceAll("[^a-zA-Z]", "");
        if (alphabeticContent.length() <= 1)
            return alphabeticContent.length();

        String LPS = "";

        for (int i = 1; i < alphabeticContent.length(); i++) {
            int low = i;
            int high = i;
            while (alphabeticContent.charAt(low) == alphabeticContent.charAt(high)) {
                low--;
                high++;
                if (low == -1 || high == alphabeticContent.length())
                    break;
            }

            String palindrome = alphabeticContent.substring(low + 1, high);
            if (palindrome.length() > LPS.length()) {
                LPS = palindrome;
            }

            low = i - 1;
            high = i;
            while (alphabeticContent.charAt(low) == alphabeticContent.charAt(high)) {
                low--;
                high++;
                if (low == -1 || high == alphabeticContent.length())
                    break;
            }

            palindrome = alphabeticContent.substring(low + 1, high);
            if (palindrome.length() > LPS.length()) {
                LPS = palindrome;
            }
        }
        return LPS.length();
    }

    private void setLongestPalindromeSize(Integer longestPalindromeSize) {
        this.longestPalindromeSize = longestPalindromeSize;
    }
}
