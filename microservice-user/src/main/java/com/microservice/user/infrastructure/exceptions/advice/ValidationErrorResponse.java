package com.microservice.user.infrastructure.exceptions.advice;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Respuesta de error para validaciones
 */
public record ValidationErrorResponse(
    String code,
    String message,
    Map<String, String> fieldErrors,
    LocalDateTime timestamp
) {
    public ValidationErrorResponse(String code, Map<String, String> fieldErrors) {
        this(code, "Validation failed", fieldErrors, LocalDateTime.now());
    }
}
