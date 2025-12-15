package com.microservice.user.domain.port.out;

import java.util.Optional;
import java.util.UUID;

/**
 * Puerto saliente - Servicio de identidad (microservice-identity)
 * Comunicación síncrona vía REST
 */
public interface IdentityServicePort {
    
    /**
     * Verifica si un usuario existe en el servicio de identidad
     */
    boolean userExists(UUID userId);
    
    /**
     * Verifica si un usuario está habilitado
     */
    boolean isUserEnabled(UUID userId);
    
    /**
     * Obtiene el estado del usuario
     */
    Optional<UserStatus> getUserStatus(UUID userId);
    
    /**
     * DTO con el estado del usuario desde identity
     */
    record UserStatus(
        UUID userId,
        String username,
        String email,
        boolean enabled,
        boolean emailVerified,
        boolean isVerified
    ) {}
}
