package com.microservice.identity.infrastructure.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

/**
 * Standard error response DTO for REST API errors.
 * Used by GlobalExceptionHandler to provide consistent error responses.
 * 
 * This class follows REST API best practices:
 * - Provides timestamp for debugging
 * - Includes HTTP status code and error name
 * - Contains user-friendly message (no sensitive data)
 * - Includes request path for context
 * - Provides unique requestId for tracing
 * 
 * Architecture:
 * - Infrastructure layer (adapter) - handles HTTP error response format
 * - Used by GlobalExceptionHandler to convert exceptions to HTTP responses
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime timestamp;

    private int status;

    private String error;

    private String message;

    private String path;

    private String requestId;

    /**
     * Default constructor.
     */
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor with all fields.
     * 
     * @param status    HTTP status code
     * @param error     HTTP status reason phrase (e.g., "Bad Request")
     * @param message   User-friendly error message
     * @param path      Request path that caused the error
     * @param requestId Unique request identifier for tracing
     */
    public ErrorResponse(int status, String error, String message, String path, String requestId) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.requestId = requestId;
    }

    /**
     * Builder for flexible construction.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int status;
        private String error;
        private String message;
        private String path;
        private String requestId;

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder error(String error) {
            this.error = error;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(status, error, message, path, requestId);
        }
    }

    // Getters and Setters

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
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

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
