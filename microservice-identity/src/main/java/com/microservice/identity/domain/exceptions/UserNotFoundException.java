package com.microservice.identity.domain.exceptions;

/**
 * Exception thrown when a user cannot be found by the specified criteria.
 * 
 * This exception is thrown by:
 * - GetUserByIdUseCase when user ID doesn't exist
 * - GetUserByEmailUseCase when email doesn't exist
 * - GetUserByUsernameUseCase when username doesn't exist
 * - DeleteUserUseCase when attempting to delete non-existent user
 * - UpdateUserProfileUseCase when attempting to update non-existent user
 * - AuthService.login() when username doesn't exist
 * - Role assignment/removal use cases when user doesn't exist
 * 
 * Mapped to HTTP 404 Not Found by GlobalExceptionHandler.
 * 
 * Security note: The detailed message (with ID/email/username) is logged
 * server-side but a generic message is returned to the client.
 */
public class UserNotFoundException extends DomainException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
