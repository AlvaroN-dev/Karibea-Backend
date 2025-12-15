package com.microservice.shopcart.infrastructure.exceptions.advice;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Validation error response with field-level details.
 */
@Schema(description = "Validation error response with field details")
public class ValidationErrorResponse {

    @Schema(description = "Error code", example = "VALIDATION_ERROR")
    private String code;

    @Schema(description = "General error message", example = "Validation failed")
    private String message;

    @Schema(description = "List of field validation errors")
    private List<FieldError> errors;

    @Schema(description = "Error timestamp", example = "2024-12-13T10:30:00Z")
    private Instant timestamp;

    @Schema(description = "Request path", example = "/api/v1/carts")
    private String path;

    public ValidationErrorResponse() {
        this.code = "VALIDATION_ERROR";
        this.message = "Validation failed";
        this.errors = new ArrayList<>();
        this.timestamp = Instant.now();
    }

    public ValidationErrorResponse(String message, List<FieldError> errors) {
        this.code = "VALIDATION_ERROR";
        this.message = message;
        this.errors = errors;
        this.timestamp = Instant.now();
    }

    public void addError(String field, String message) {
        this.errors.add(new FieldError(field, message));
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

    public List<FieldError> getErrors() {
        return errors;
    }

    public void setErrors(List<FieldError> errors) {
        this.errors = errors;
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

    /**
     * Represents a single field validation error.
     */
    @Schema(description = "Field validation error")
    public static class FieldError {

        @Schema(description = "Field name that failed validation", example = "quantity")
        private String field;

        @Schema(description = "Validation error message", example = "Quantity must be at least 1")
        private String message;

        public FieldError() {
        }

        public FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
