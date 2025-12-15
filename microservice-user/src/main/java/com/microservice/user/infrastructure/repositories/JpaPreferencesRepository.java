package com.microservice.user.infrastructure.repositories;

import com.microservice.user.infrastructure.entities.UserPreferencesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para preferencias del usuario
 */
@Repository
public interface JpaPreferencesRepository extends JpaRepository<UserPreferencesEntity, UUID> {
    
    Optional<UserPreferencesEntity> findByExternalUserId(UUID externalUserId);
    
    Optional<UserPreferencesEntity> findByExternalUserIdAndDeletedFalse(UUID externalUserId);
    
    boolean existsByExternalUserId(UUID externalUserId);
    
    void deleteByExternalUserId(UUID externalUserId);
}
