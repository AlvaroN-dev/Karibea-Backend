package com.microservice.identity.domain.exceptions;

/**
 * Exception thrown when attempting to register a user with an email that
 * already exists.
 * 
 * This exception is thrown by:
 * - UserService.validateEmailUniqueness() during user registration
 * - UserService.updateEmail() when changing to an existing email
 * 
 * Mapped to HTTP 409 Conflict by GlobalExceptionHandler.
 * 
 * Security note: The error message should not expose the actual email address
 * to prevent email enumeration attacks.
 */
public class EmailAlreadyExistsException extends DomainException {
    public EmailAlreadyExistsException(String email) {
        super("Email already exists");
    }
}
