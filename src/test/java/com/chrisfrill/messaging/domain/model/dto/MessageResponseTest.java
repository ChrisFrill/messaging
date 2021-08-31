package com.chrisfrill.messaging.domain.model.dto;

import com.chrisfrill.messaging.TestData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MessageResponseTest extends TestData {
    @Test
    public void calculateLongestPalindromeSize_Returns1_ForSingleAlphabeticChar() {
        assertEquals(new MessageResponse("a", dateTime).getLongestPalindromeSize(), 1);
    }

    @Test
    public void calculateLongestPalindromeSize_Returns2_ForDuplicateAlphabeticChars() {
        assertEquals(new MessageResponse("aa", dateTime).getLongestPalindromeSize(), 2);
    }

    @Test
    public void calculateLongestPalindromeSize_Returns1_ForDistantAlphabeticChars() {
        assertEquals(new MessageResponse("ab", dateTime).getLongestPalindromeSize(), 1);
    }

    @Test
    public void calculateLongestPalindromeSize_Returns0_ForAlphanumericChar() {
        assertEquals(new MessageResponse("0123456789", dateTime).getLongestPalindromeSize(), 0);
    }

    @Test
    public void calculateLongestPalindromeSize_Returns0_ForSpecialChars() {
        assertEquals(new MessageResponse("\\.[]{}()<>*+-=!?^$|", dateTime).getLongestPalindromeSize(), 0);
    }

    @Test
    public void calculateLongestPalindromeSize_Returns0_ForBlankString() {
        assertEquals(new MessageResponse("", dateTime).getLongestPalindromeSize(), 0);
    }

    @Test
    public void calculateLongestPalindromeSize_ThrowsRuntimeException_ForNull() {
        assertThrows(RuntimeException.class, () -> new MessageResponse(null, dateTime).getLongestPalindromeSize());
    }

    @Test
    public void calculateLongestPalindromeSize_ReturnsCorrectLength_ForCombinedChars() {
        assertEquals(new MessageResponse("aab1.2|3ba-a", dateTime).getLongestPalindromeSize(), 6);
    }
}