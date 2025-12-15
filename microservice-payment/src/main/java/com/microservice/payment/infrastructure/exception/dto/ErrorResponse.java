package com.microservice.payment.infrastructure.exception.dto;

import java.time.LocalDateTime;

/**
 * Standard error response DTO for API errors.
 * 
 * <p>
 * <b>Technical Note:</b> usage of Record (DTO).
 * </p>
 * <p>
 * This object is a <b>DTO (Data Transfer Object)</b>, NOT an Exception.
 * </p>
 * <ul>
 * <li><b>DTO:</b> Simple data carrier to transport structure (JSON) to the
 * client.</li>
 * <li><b>Exception:</b> Java class (extends Throwable) that interrupts
 * flow.</li>
 * </ul>
 * We use a Record because it is immutable and concise for data transport.
 */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String code,
        String message,
        String path) {

    public static ErrorResponse of(int status, String error, String code, String message, String path) {
        return new ErrorResponse(LocalDateTime.now(), status, error, code, message, path);
    }
}
