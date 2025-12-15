package com.microservice.user.domain.port.in;

import java.util.UUID;

/**
 * Puerto entrante - Caso de uso para eliminar perfil de usuario (soft delete)
 */
public interface DeleteUserProfileUseCase {
    
    void execute(UUID profileId);
    
    void executeByExternalUserId(UUID externalUserId);
}
