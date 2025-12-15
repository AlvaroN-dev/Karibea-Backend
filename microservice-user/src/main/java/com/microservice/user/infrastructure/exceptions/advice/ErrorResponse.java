package com.microservice.user.infrastructure.exceptions.advice;

import java.time.LocalDateTime;

/**
 * Respuesta de error genérica
 */
public record ErrorResponse(
    String code,
    String message,
    LocalDateTime timestamp
) {
    public ErrorResponse(String code, String message) {
        this(code, message, LocalDateTime.now());
    }
}
