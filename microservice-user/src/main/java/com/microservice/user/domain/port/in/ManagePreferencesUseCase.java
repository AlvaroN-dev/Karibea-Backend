package com.microservice.user.domain.port.in;

import com.microservice.user.domain.models.UserPreferences;

import java.util.Optional;
import java.util.UUID;

/**
 * Puerto entrante - Caso de uso para gestionar preferencias del usuario
 */
public interface ManagePreferencesUseCase {
    
    UserPreferences create(UUID externalUserId);
    
    UserPreferences update(UpdatePreferencesCommand command);
    
    Optional<UserPreferences> findByExternalUserId(UUID externalUserId);
    
    void delete(UUID externalUserId);
    
    record UpdatePreferencesCommand(
        UUID externalUserId,
        UUID languageId,
        UUID currencyId,
        Boolean notificationEmail,
        Boolean notificationPush
    ) {
        public UpdatePreferencesCommand {
            if (externalUserId == null) {
                throw new IllegalArgumentException("External user ID is required");
            }
        }
    }
}
