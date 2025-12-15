package com.microservice.user.domain.exceptions;

import java.util.UUID;

/**
 * Excepción cuando ya existe un perfil para el usuario
 */
public class UserProfileAlreadyExistsException extends DomainException {
    
    private static final String CODE = "USER_PROFILE_ALREADY_EXISTS";
    
    public UserProfileAlreadyExistsException(UUID externalUserId) {
        super(CODE, String.format("User profile already exists for external user ID '%s'", externalUserId));
    }
}
