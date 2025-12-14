package com.microservice.shopcart.infrastructure.exceptions.advice;

import com.microservice.shopcart.application.exception.CartOperationException;
import com.microservice.shopcart.application.exception.ExternalServiceException;
import com.microservice.shopcart.domain.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * Global exception handler for the Shopping Cart microservice.
 * Centralizes error handling and provides consistent error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ==================== DOMAIN EXCEPTIONS ====================

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCartNotFound(
            CartNotFoundException ex, HttpServletRequest request) {
        log.warn("Cart not found: {}", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            "CART_NOT_FOUND",
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleItemNotFound(
            ItemNotFoundException ex, HttpServletRequest request) {
        log.warn("Item not found: {}", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            "ITEM_NOT_FOUND",
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCouponNotFound(
            CouponNotFoundException ex, HttpServletRequest request) {
        log.warn("Coupon not found: {}", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            "COUPON_NOT_FOUND",
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(CartExpiredException.class)
    public ResponseEntity<ErrorResponse> handleCartExpired(
            CartExpiredException ex, HttpServletRequest request) {
        log.warn("Cart expired: {}", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            "CART_EXPIRED",
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.GONE).body(error);
    }

    @ExceptionHandler(CouponAlreadyAppliedException.class)
    public ResponseEntity<ErrorResponse> handleCouponAlreadyApplied(
            CouponAlreadyAppliedException ex, HttpServletRequest request) {
        log.warn("Coupon already applied: {}", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            "COUPON_ALREADY_APPLIED",
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(InvalidCartOperationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCartOperation(
            InvalidCartOperationException ex, HttpServletRequest request) {
        log.warn("Invalid cart operation: {}", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            "INVALID_CART_OPERATION",
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(InvalidQuantityException.class)
    public ResponseEntity<ErrorResponse> handleInvalidQuantity(
            InvalidQuantityException ex, HttpServletRequest request) {
        log.warn("Invalid quantity: {}", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            "INVALID_QUANTITY",
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // ==================== APPLICATION EXCEPTIONS ====================

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ErrorResponse> handleExternalService(
            ExternalServiceException ex, HttpServletRequest request) {
        log.error("External service error: {}", ex.getMessage(), ex);
        
        ErrorResponse error = new ErrorResponse(
            "EXTERNAL_SERVICE_ERROR",
            "External service temporarily unavailable. Please try again later.",
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    @ExceptionHandler(CartOperationException.class)
    public ResponseEntity<ErrorResponse> handleCartOperation(
            CartOperationException ex, HttpServletRequest request) {
        log.error("Cart operation error: {}", ex.getMessage(), ex);
        
        ErrorResponse error = new ErrorResponse(
            "CART_OPERATION_ERROR",
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // ==================== VALIDATION EXCEPTIONS ====================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.warn("Validation error: {}", ex.getMessage());
        
        List<ValidationErrorResponse.FieldError> fieldErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> new ValidationErrorResponse.FieldError(
                error.getField(),
                error.getDefaultMessage()))
            .toList();

        ValidationErrorResponse error = new ValidationErrorResponse(
            "Validation failed for " + fieldErrors.size() + " field(s)",
            fieldErrors
        );
        error.setPath(request.getRequestURI());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("Illegal argument: {}", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            "INVALID_ARGUMENT",
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // ==================== GENERIC EXCEPTIONS ====================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex, HttpServletRequest request) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        
        ErrorResponse error = new ErrorResponse(
            "INTERNAL_ERROR",
            "An unexpected error occurred. Please try again later.",
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
