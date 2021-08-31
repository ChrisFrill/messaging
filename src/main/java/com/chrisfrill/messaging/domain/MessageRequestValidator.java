package com.chrisfrill.messaging.domain;

import com.chrisfrill.messaging.domain.model.dto.MessageRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class MessageRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return MessageRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "content", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "timestamp", "field.required");
    }
}