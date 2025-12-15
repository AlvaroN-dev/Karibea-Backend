package com.microservice.identity.infrastructure.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Validation-specific error response DTO.
 * Extends ErrorResponse to include field-level validation errors.
 * 
 * Used when request validation fails (e.g., @Valid annotation on DTOs).
 * Provides detailed information about which fields failed validation and why.
 * 
 * Example response:
 * {
 * "timestamp": "2025-12-07T07:55:00.000Z",
 * "status": 400,
 * "error": "Bad Request",
 * "message": "Validation failed",
 * "path": "/api/v1/auth/register",
 * "requestId": "uuid-here",
 * "fieldErrors": [
 * {
 * "field": "email",
 * "rejectedValue": "invalid-email",
 * "message": "Email must be valid"
 * }
 * ]
 * }
 * 
 * Architecture:
 * - Infrastructure layer (adapter) - handles HTTP validation error response
 * format
 * - Used by GlobalExceptionHandler for MethodArgumentNotValidException
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationErrorResponse extends ErrorResponse {

    private List<FieldError> fieldErrors;

    /**
     * Default constructor.
     */
    public ValidationErrorResponse() {
        super();
        this.fieldErrors = new ArrayList<>();
    }

    /**
     * Constructor with base error information.
     * 
     * @param status    HTTP status code
     * @param error     HTTP status reason phrase
     * @param message   User-friendly error message
     * @param path      Request path that caused the error
     * @param requestId Unique request identifier
     */
    public ValidationErrorResponse(int status, String error, String message, String path, String requestId) {
        super(status, error, message, path, requestId);
        this.fieldErrors = new ArrayList<>();
    }

    /**
     * Add a field error to the response.
     * 
     * @param field         Field name that failed validation
     * @param rejectedValue Value that was rejected
     * @param message       Validation error message
     */
    public void addFieldError(String field, Object rejectedValue, String message) {
        this.fieldErrors.add(new FieldError(field, rejectedValue, message));
    }

    /**
     * Add a field error to the response.
     * 
     * @param fieldError Field error object
     */
    public void addFieldError(FieldError fieldError) {
        this.fieldErrors.add(fieldError);
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    /**
     * Represents a single field validation error.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FieldError {
        private String field;
        private Object rejectedValue;
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
