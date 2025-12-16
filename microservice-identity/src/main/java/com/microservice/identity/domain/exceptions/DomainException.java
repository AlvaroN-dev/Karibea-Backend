package com.microservice.identity.domain.exceptions;

/**
 * Base exception for all domain-layer exceptions.
 * Represents violations of business rules or domain constraints.
 * 
 * This exception follows hexagonal architecture principles:
 * - Domain layer - represents business rule violations
 * - Independent of infrastructure concerns (HTTP, database, etc.)
 * - Mapped to HTTP responses by GlobalExceptionHandler in infrastructure layer
 * 
 * All specific domain exceptions should extend this class.
 * 
 * @see EmailAlreadyExistsException
 * @see InvalidCredentialsException
 * @see UserNotFoundException
 * @see RoleNotFoundException
 */
public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}
