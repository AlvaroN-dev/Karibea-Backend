package com.microservice.identity.domain.exceptions;

/**
 * Exception thrown when user provides invalid login credentials.
 * 
 * This exception is thrown by:
 * - AuthService.login() when username/password combination is incorrect
 * 
 * Mapped to HTTP 401 Unauthorized by GlobalExceptionHandler.
 * 
 * Security note: The error message is intentionally generic to prevent
 * username enumeration attacks. It doesn't reveal whether the username
 * or password was incorrect.
 */
public class InvalidCredentialsException extends DomainException {
    public InvalidCredentialsException() {
        super("Invalid credentials provided");
    }
}
