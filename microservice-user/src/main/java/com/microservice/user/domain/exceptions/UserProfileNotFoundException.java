package com.microservice.user.domain.exceptions;

import java.util.UUID;

/**
 * Excepción cuando no se encuentra un perfil de usuario
 */
public class UserProfileNotFoundException extends DomainException {
    
    private static final String CODE = "USER_PROFILE_NOT_FOUND";
    
    public UserProfileNotFoundException(UUID id) {
        super(CODE, String.format("User profile with ID '%s' not found", id));
    }
    
    public UserProfileNotFoundException(String message) {
        super(CODE, message);
    }
    
    public static UserProfileNotFoundException byExternalUserId(UUID externalUserId) {
        return new UserProfileNotFoundException(
            String.format("User profile for external user ID '%s' not found", externalUserId)
        );
    }
}
