package com.microservice.identity.domain.exceptions;

/**
 * Exception thrown when a role cannot be found by the specified criteria.
 * 
 * This exception will be thrown by:
 * - GetRoleByIdUseCase when role ID doesn't exist
 * - GetRoleByNameUseCase when role name doesn't exist
 * - DeleteRoleUseCase when attempting to delete non-existent role
 * - Role assignment use cases when role doesn't exist
 * 
 * Mapped to HTTP 404 Not Found by GlobalExceptionHandler.
 * 
 * Note: This exception is currently defined but not yet used in the codebase.
 * It's kept for future role management feature expansion.
 */
public class RoleNotFoundException extends DomainException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
