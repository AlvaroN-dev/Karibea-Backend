package com.microservice.identity.infrastructure.exceptions.advice;

import com.microservice.identity.domain.exceptions.DomainException;
import com.microservice.identity.domain.exceptions.EmailAlreadyExistsException;
import com.microservice.identity.domain.exceptions.InvalidCredentialsException;
import com.microservice.identity.domain.exceptions.RoleNotFoundException;
import com.microservice.identity.domain.exceptions.UserNotFoundException;
import com.microservice.identity.domain.exceptions.EventConsumptionException;
import com.microservice.identity.domain.exceptions.EventPublishingException;
import com.microservice.identity.infrastructure.exceptions.ErrorResponse;
import com.microservice.identity.infrastructure.exceptions.ValidationErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.UUID;

/**
 * Global exception handler for the microservice.
 * Catches all exceptions thrown by controllers and converts them to appropriate
 * HTTP responses.
 * 
 * This class follows hexagonal architecture principles:
 * - Infrastructure layer (adapter) - handles HTTP concerns
 * - Maps domain exceptions to HTTP status codes
 * - Provides consistent error response format
 * - Ensures no sensitive data is exposed to clients
 * 
 * Security considerations:
 * - Never expose stack traces to clients
 * - Never expose internal paths or database details
 * - Log detailed errors server-side for debugging
 * - Return generic messages for unexpected errors
 * 
 * @see ErrorResponse
 * @see ValidationErrorResponse
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ==================== Domain Exception Handlers ====================

    /**
     * Handles EmailAlreadyExistsException.
     * Returns 409 Conflict when attempting to register with an existing email.
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(
            EmailAlreadyExistsException ex,
            HttpServletRequest request) {

        String requestId = generateRequestId();
        log.warn("Email already exists - RequestId: {}, Path: {}", requestId, request.getRequestURI());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message("Email already exists")
                .path(request.getRequestURI())
                .requestId(requestId)
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    /**
     * Handles InvalidCredentialsException.
     * Returns 401 Unauthorized when login credentials are invalid.
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(
            InvalidCredentialsException ex,
            HttpServletRequest request) {

        String requestId = generateRequestId();
        log.warn("Invalid credentials - RequestId: {}, Path: {}", requestId, request.getRequestURI());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message("Invalid credentials provided")
                .path(request.getRequestURI())
                .requestId(requestId)
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    /**
     * Handles UserNotFoundException.
     * Returns 404 Not Found when a user cannot be found.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(
            UserNotFoundException ex,
            HttpServletRequest request) {

        String requestId = generateRequestId();
        log.warn("User not found - RequestId: {}, Path: {}, Message: {}",
                requestId, request.getRequestURI(), ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message("User not found")
                .path(request.getRequestURI())
                .requestId(requestId)
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Handles RoleNotFoundException.
     * Returns 404 Not Found when a role cannot be found.
     */
    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoleNotFound(
            RoleNotFoundException ex,
            HttpServletRequest request) {

        String requestId = generateRequestId();
        log.warn("Role not found - RequestId: {}, Path: {}, Message: {}",
                requestId, request.getRequestURI(), ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message("Role not found")
                .path(request.getRequestURI())
                .requestId(requestId)
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Handles generic DomainException (catch-all for domain exceptions).
     * Returns 400 Bad Request for unspecified domain errors.
     */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(
            DomainException ex,
            HttpServletRequest request) {

        String requestId = generateRequestId();
        log.warn("Domain exception - RequestId: {}, Path: {}, Message: {}",
                requestId, request.getRequestURI(), ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .requestId(requestId)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // ==================== Event Exception Handlers ====================

    /**
     * Handles EventPublishingException.
     * Returns 500 Internal Server Error (event publishing failures are server-side
     * issues).
     * Logs detailed error information but returns generic message to client.
     */
    @ExceptionHandler(EventPublishingException.class)
    public ResponseEntity<ErrorResponse> handleEventPublishingException(
            EventPublishingException ex,
            HttpServletRequest request) {

        String requestId = generateRequestId();
        log.error("Event publishing failed - RequestId: {}, EventId: {}, EventType: {}, Topic: {}, Message: {}",
                requestId, ex.getEventId(), ex.getEventType(), ex.getTopic(), ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("An error occurred while processing your request")
                .path(request.getRequestURI())
                .requestId(requestId)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Handles EventConsumptionException.
     * Returns 500 Internal Server Error (event consumption failures are server-side
     * issues).
     * Logs detailed error information but returns generic message to client.
     */
    @ExceptionHandler(EventConsumptionException.class)
    public ResponseEntity<ErrorResponse> handleEventConsumptionException(
            EventConsumptionException ex,
            HttpServletRequest request) {

        String requestId = generateRequestId();
        log.error(
                "Event consumption failed - RequestId: {}, EventId: {}, EventType: {}, Topic: {}, RetryCount: {}, Message: {}",
                requestId, ex.getEventId(), ex.getEventType(), ex.getTopic(), ex.getRetryCount(), ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("An error occurred while processing your request")
                .path(request.getRequestURI())
                .requestId(requestId)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    // ==================== Spring Framework Exception Handlers ====================

    /**
     * Handles MethodArgumentNotValidException.
     * Returns 400 Bad Request with detailed field validation errors.
     * Triggered by @Valid annotation on request DTOs.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String requestId = generateRequestId();
        log.warn("Validation failed - RequestId: {}, Path: {}, ErrorCount: {}",
                requestId, request.getRequestURI(), ex.getBindingResult().getErrorCount());

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation failed",
                request.getRequestURI(),
                requestId);

        // Add field errors
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addFieldError(
                    fieldError.getField(),
                    fieldError.getRejectedValue(),
                    fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles HttpMessageNotReadableException.
     * Returns 400 Bad Request when request body cannot be parsed (e.g., malformed
     * JSON).
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        String requestId = generateRequestId();
        log.warn("Malformed request body - RequestId: {}, Path: {}", requestId, request.getRequestURI());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Malformed request body")
                .path(request.getRequestURI())
                .requestId(requestId)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles MethodArgumentTypeMismatchException.
     * Returns 400 Bad Request when a path variable or request parameter has the
     * wrong type.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        String requestId = generateRequestId();
        log.warn("Type mismatch - RequestId: {}, Path: {}, Parameter: {}",
                requestId, request.getRequestURI(), ex.getName());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Invalid parameter type")
                .path(request.getRequestURI())
                .requestId(requestId)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles MissingServletRequestParameterException.
     * Returns 400 Bad Request when a required request parameter is missing.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameter(
            MissingServletRequestParameterException ex,
            HttpServletRequest request) {

        String requestId = generateRequestId();
        log.warn("Missing request parameter - RequestId: {}, Path: {}, Parameter: {}",
                requestId, request.getRequestURI(), ex.getParameterName());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Missing required parameter: " + ex.getParameterName())
                .path(request.getRequestURI())
                .requestId(requestId)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles AccessDeniedException.
     * Returns 403 Forbidden when user doesn't have permission to access a resource.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request) {

        String requestId = generateRequestId();
        log.warn("Access denied - RequestId: {}, Path: {}", requestId, request.getRequestURI());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .message("Access denied")
                .path(request.getRequestURI())
                .requestId(requestId)
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    /**
     * Handles AuthenticationException.
     * Returns 401 Unauthorized when authentication fails.
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            AuthenticationException ex,
            HttpServletRequest request) {

        String requestId = generateRequestId();
        log.warn("Authentication failed - RequestId: {}, Path: {}", requestId, request.getRequestURI());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message("Authentication failed")
                .path(request.getRequestURI())
                .requestId(requestId)
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    // ==================== Generic Exception Handler ====================

    /**
     * Handles all other exceptions (catch-all).
     * Returns 500 Internal Server Error with generic message.
     * Logs detailed error information for debugging but doesn't expose it to
     * client.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        String requestId = generateRequestId();
        log.error("Unexpected error - RequestId: {}, Path: {}, Exception: {}",
                requestId, request.getRequestURI(), ex.getClass().getName(), ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("An unexpected error occurred")
                .path(request.getRequestURI())
                .requestId(requestId)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    // ==================== Helper Methods ====================

    /**
     * Generates a unique request ID for tracing.
     * Used to correlate client errors with server logs.
     * 
     * @return Unique request ID
     */
    private String generateRequestId() {
        return UUID.randomUUID().toString();
    }
}
