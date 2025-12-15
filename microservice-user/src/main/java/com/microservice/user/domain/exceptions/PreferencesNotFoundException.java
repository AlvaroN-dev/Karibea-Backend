package com.microservice.user.domain.exceptions;

import java.util.UUID;

/**
 * Excepción cuando no se encuentran preferencias del usuario
 */
public class PreferencesNotFoundException extends DomainException {
    
    private static final String CODE = "PREFERENCES_NOT_FOUND";
    
    public PreferencesNotFoundException(UUID externalUserId) {
        super(CODE, String.format("Preferences for user ID '%s' not found", externalUserId));
    }
}
