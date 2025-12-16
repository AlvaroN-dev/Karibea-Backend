package com.microservice.inventory.infrastructure.exceptions.advice;

import java.time.LocalDateTime;
import java.util.List;

public record ValidationErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldError> fieldErrors) {
    public record FieldError(String field, String message) {
    }

    public static ValidationErrorResponse of(int status, String message, String path, List<FieldError> fieldErrors) {
        return new ValidationErrorResponse(LocalDateTime.now(), status, "Validation Failed", message, path,
                fieldErrors);
    }
}
