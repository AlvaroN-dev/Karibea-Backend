package com.microservice.order.infrastructure.exceptions.advice;

import com.microservice.order.application.exception.OrderNotFoundException;
import com.microservice.order.domain.exceptions.InvalidOrderStateTransitionException;
import com.microservice.order.domain.exceptions.OrderDomainException;
import com.microservice.order.domain.exceptions.OrderInvariantViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Global exception handler for REST controllers.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle domain exceptions - 422 Unprocessable Entity.
     */
    @ExceptionHandler(OrderDomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(OrderDomainException ex) {
        log.warn("Domain rule violation: {}", ex.getMessage());
        return ResponseEntity
                .unprocessableEntity()
                .body(ErrorResponse.of(ex.getCode(), ex.getMessage()));
    }

    /**
     * Handle invalid state transitions - 409 Conflict.
     */
    @ExceptionHandler(InvalidOrderStateTransitionException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTransition(InvalidOrderStateTransitionException ex) {
        log.warn("Invalid state transition: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of(ex.getCode(), ex.getMessage()));
    }

    /**
     * Handle invariant violations - 422 Unprocessable Entity.
     */
    @ExceptionHandler(OrderInvariantViolationException.class)
    public ResponseEntity<ErrorResponse> handleInvariantViolation(OrderInvariantViolationException ex) {
        log.warn("Invariant violation: {}", ex.getMessage());
        return ResponseEntity
                .unprocessableEntity()
                .body(ErrorResponse.of(ex.getCode(), ex.getMessage()));
    }

    /**
     * Handle order not found - 404 Not Found.
     */
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(OrderNotFoundException ex) {
        log.info("Order not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of("ORDER_NOT_FOUND", ex.getMessage()));
    }

    /**
     * Handle validation errors - 400 Bad Request.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<ValidationErrorResponse.FieldError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ValidationErrorResponse.FieldError(fe.getField(), fe.getDefaultMessage()))
                .toList();

        log.info("Validation error: {} field(s) invalid", errors.size());

        return ResponseEntity
                .badRequest()
                .body(new ValidationErrorResponse("VALIDATION_ERROR", errors, Instant.now()));
    }

    /**
     * Handle illegal argument - 400 Bad Request.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Invalid argument: {}", ex.getMessage());
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of("INVALID_ARGUMENT", ex.getMessage()));
    }

    /**
     * Handle generic exceptions - 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of("INTERNAL_ERROR", "An unexpected error occurred"));
    }

    // ========== Response DTOs ==========

    public record ErrorResponse(
            String code,
            String message,
            Instant timestamp,
            String traceId) {
        public static ErrorResponse of(String code, String message) {
            return new ErrorResponse(code, message, Instant.now(), UUID.randomUUID().toString());
        }
    }

    public record ValidationErrorResponse(
            String code,
            List<FieldError> errors,
            Instant timestamp) {
        public record FieldError(String field, String message) {
        }
    }
}
