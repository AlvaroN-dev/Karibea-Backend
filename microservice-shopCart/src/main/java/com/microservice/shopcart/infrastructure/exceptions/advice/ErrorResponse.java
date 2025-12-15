package com.microservice.shopcart.infrastructure.exceptions.advice;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Standard error response for API errors.
 */
@Schema(description = "Standard error response")
public class ErrorResponse {

    @Schema(description = "Error code", example = "CART_NOT_FOUND")
    private String code;

    @Schema(description = "Error message", example = "Shopping cart with ID 'xyz' not found")
    private String message;

    @Schema(description = "Error timestamp", example = "2024-12-13T10:30:00Z")
    private Instant timestamp;

    @Schema(description = "Request path", example = "/api/v1/carts/550e8400-e29b-41d4-a716-446655440000")
    private String path;

    public ErrorResponse() {
        this.timestamp = Instant.now();
    }

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = Instant.now();
    }

    public ErrorResponse(String code, String message, String path) {
        this.code = code;
        this.message = message;
        this.path = path;
        this.timestamp = Instant.now();
    }

    // Getters and Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
