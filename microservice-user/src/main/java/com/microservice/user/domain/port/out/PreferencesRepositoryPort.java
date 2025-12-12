package com.microservice.user.domain.port.out;

import com.microservice.user.domain.models.UserPreferences;

import java.util.Optional;
import java.util.UUID;

/**
 * Puerto saliente - Repositorio de preferencias
 */
public interface PreferencesRepositoryPort {
    
    UserPreferences save(UserPreferences preferences);
    
    Optional<UserPreferences> findByExternalUserId(UUID externalUserId);
    
    void deleteByExternalUserId(UUID externalUserId);
    
    boolean existsByExternalUserId(UUID externalUserId);
}
