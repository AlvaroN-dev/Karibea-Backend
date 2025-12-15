package com.microservice.catalog.infrastructure.exceptions.advice;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * Validation error response DTO with field-level details.
 */
@Schema(description = "Validation error response")
public class ValidationErrorResponse extends ErrorResponse {

    @Schema(description = "List of field validation errors")
    private List<FieldError> fieldErrors;

    // Constructors
    public ValidationErrorResponse() {
        super();
        this.fieldErrors = new ArrayList<>();
    }

    public ValidationErrorResponse(int status, String code, String message, String path) {
        super(status, code, message, path);
        this.fieldErrors = new ArrayList<>();
    }

    // Methods
    public void addFieldError(String field, Object rejectedValue, String message) {
        this.fieldErrors.add(new FieldError(field, rejectedValue, message));
    }

    // Getters and Setters
    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    /**
     * Represents a single field validation error.
     */
    @Schema(description = "Field validation error")
    public static class FieldError {

        @Schema(description = "Field name", example = "name")
        private String field;

        @Schema(description = "Rejected value", example = "")
        private Object rejectedValue;

        @Schema(description = "Error message", example = "Name is required")
        private String message;

        public FieldError() {
        }

        public FieldError(String field, Object rejectedValue, String message) {
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public Object getRejectedValue() {
            return rejectedValue;
        }

        public void setRejectedValue(Object rejectedValue) {
            this.rejectedValue = rejectedValue;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
