package com.microservice.chatbot.infrastructure.exceptions.advice;

import com.microservice.chatbot.application.exception.ChatApplicationException;
import com.microservice.chatbot.domain.exceptions.AIProviderException;
import com.microservice.chatbot.domain.exceptions.ChatDomainException;
import com.microservice.chatbot.domain.exceptions.ConversationNotFoundException;
import com.microservice.chatbot.domain.exceptions.InvalidMessageException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler for the chatbot API.
 * Location: infrastructure/exceptions/advice - Centralized error handling.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

        /**
         * Handles validation errors from @Valid annotations.
         */
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ValidationErrorResponse> handleValidationException(
                        MethodArgumentNotValidException ex, HttpServletRequest request) {

                log.warn("Validation error: {}", ex.getMessage());

                List<ValidationErrorResponse.FieldError> fieldErrors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(error -> ValidationErrorResponse.FieldError.builder()
                                                .field(error.getField())
                                                .rejectedValue(error.getRejectedValue() != null
                                                                ? error.getRejectedValue().toString()
                                                                : null)
                                                .message(error.getDefaultMessage())
                                                .build())
                                .collect(Collectors.toList());

                ValidationErrorResponse response = ValidationErrorResponse.of(
                                HttpStatus.BAD_REQUEST.value(),
                                request.getRequestURI(),
                                fieldErrors);

                return ResponseEntity.badRequest().body(response);
        }

        /**
         * Handles conversation not found exceptions.
         */
        @ExceptionHandler(ConversationNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleConversationNotFound(
                        ConversationNotFoundException ex, HttpServletRequest request) {

                log.warn("Conversation not found: {}", ex.getConversationId());

                ErrorResponse response = ErrorResponse.of(
                                HttpStatus.NOT_FOUND.value(),
                                "Not Found",
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        /**
         * Handles invalid message exceptions.
         */
        @ExceptionHandler(InvalidMessageException.class)
        public ResponseEntity<ErrorResponse> handleInvalidMessage(
                        InvalidMessageException ex, HttpServletRequest request) {

                log.warn("Invalid message: {}", ex.getMessage());

                ErrorResponse response = ErrorResponse.of(
                                HttpStatus.BAD_REQUEST.value(),
                                "Bad Request",
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.badRequest().body(response);
        }

        /**
         * Handles AI provider exceptions.
         */
        @ExceptionHandler(AIProviderException.class)
        public ResponseEntity<ErrorResponse> handleAIProviderException(
                        AIProviderException ex, HttpServletRequest request) {

                log.error("AI provider error: {}", ex.getMessage(), ex);

                ErrorResponse response = ErrorResponse.of(
                                HttpStatus.SERVICE_UNAVAILABLE.value(),
                                "Service Unavailable",
                                "The AI service is temporarily unavailable. Please try again later.",
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }

        /**
         * Handles domain exceptions.
         */
        @ExceptionHandler(ChatDomainException.class)
        public ResponseEntity<ErrorResponse> handleDomainException(
                        ChatDomainException ex, HttpServletRequest request) {

                log.warn("Domain error: {}", ex.getMessage());

                ErrorResponse response = ErrorResponse.of(
                                HttpStatus.BAD_REQUEST.value(),
                                "Bad Request",
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.badRequest().body(response);
        }

        /**
         * Handles application exceptions.
         */
        @ExceptionHandler(ChatApplicationException.class)
        public ResponseEntity<ErrorResponse> handleApplicationException(
                        ChatApplicationException ex, HttpServletRequest request) {

                log.error("Application error: {}", ex.getMessage(), ex);

                ErrorResponse response = ErrorResponse.of(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Internal Server Error",
                                "An unexpected error occurred. Please try again.",
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        /**
         * Handles all other exceptions.
         */
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGenericException(
                        Exception ex, HttpServletRequest request) {

                log.error("Unexpected error: {}", ex.getMessage(), ex);

                ErrorResponse response = ErrorResponse.of(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Internal Server Error",
                                "An unexpected error occurred. Please try again.",
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
}
