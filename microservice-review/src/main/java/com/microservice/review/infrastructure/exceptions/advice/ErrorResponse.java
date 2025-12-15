package com.microservice.review.infrastructure.exceptions.advice;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Standard error response DTO.
 */
@Schema(description = "Error response")
public class ErrorResponse {

    @Schema(description = "Timestamp of the error", example = "2024-01-15T10:30:00Z")
    private Instant timestamp;

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "Error code", example = "REVIEW_NOT_FOUND")
    private String code;

    @Schema(description = "Error message", example = "Review not found with ID: 123e4567-e89b-12d3-a456-426614174000")
    private String message;

    @Schema(description = "Request path", example = "/api/v1/reviews/123e4567-e89b-12d3-a456-426614174000")
    private String path;

    @Schema(description = "Trace ID for debugging", example = "abc123def456")
    private String traceId;

    // Constructors
    public ErrorResponse() {
        this.timestamp = Instant.now();
    }

    public ErrorResponse(int status, String code, String message, String path) {
        this.timestamp = Instant.now();
        this.status = status;
        this.code = code;
        this.message = message;
        this.path = path;
    }

    // Getters and Setters
    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
