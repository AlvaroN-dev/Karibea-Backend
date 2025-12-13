package com.microservice.chatbot.infrastructure.exceptions.advice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Validation error response with field-level details.
 * Location: infrastructure/exceptions/advice - Validation error response DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorResponse {

    private int status;
    private String error;
    private String message;
    private String path;
    private List<FieldError> fieldErrors;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldError {
        private String field;
        private String rejectedValue;
        private String message;
    }

    public static ValidationErrorResponse of(int status, String path, List<FieldError> fieldErrors) {
        return ValidationErrorResponse.builder()
                .status(status)
                .error("Validation Failed")
                .message("Request validation failed. Check field errors for details.")
                .path(path)
                .fieldErrors(fieldErrors)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
