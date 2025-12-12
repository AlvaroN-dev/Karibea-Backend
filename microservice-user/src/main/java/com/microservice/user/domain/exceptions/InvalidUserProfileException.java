package com.microservice.user.domain.exceptions;

/**
 * Excepción cuando los datos del perfil son inválidos
 */
public class InvalidUserProfileException extends DomainException {
    
    private static final String CODE = "INVALID_USER_PROFILE";
    
    public InvalidUserProfileException(String message) {
        super(CODE, message);
    }
}
