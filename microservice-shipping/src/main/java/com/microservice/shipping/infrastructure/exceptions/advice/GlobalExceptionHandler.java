package com.microservice.shipping.infrastructure.exceptions.advice;

import com.microservice.shipping.application.exception.ShipmentNotFoundException;
import com.microservice.shipping.domain.exceptions.InvalidShipmentStateTransitionException;
import com.microservice.shipping.domain.exceptions.ShipmentDomainException;
import com.microservice.shipping.domain.exceptions.ShipmentInvariantViolationException;
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

    @ExceptionHandler(ShipmentDomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(ShipmentDomainException ex) {
        log.warn("Domain rule violation: {}", ex.getMessage());
        return ResponseEntity
                .unprocessableEntity()
                .body(ErrorResponse.of(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(InvalidShipmentStateTransitionException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTransition(InvalidShipmentStateTransitionException ex) {
        log.warn("Invalid state transition: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(ShipmentInvariantViolationException.class)
    public ResponseEntity<ErrorResponse> handleInvariantViolation(ShipmentInvariantViolationException ex) {
        log.warn("Invariant violation: {}", ex.getMessage());
        return ResponseEntity
                .unprocessableEntity()
                .body(ErrorResponse.of(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(ShipmentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ShipmentNotFoundException ex) {
        log.info("Shipment not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of("SHIPMENT_NOT_FOUND", ex.getMessage()));
    }

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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Invalid argument: {}", ex.getMessage());
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of("INVALID_ARGUMENT", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of("INTERNAL_ERROR", "An unexpected error occurred"));
    }

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
