package com.microservice.user.infrastructure.repositories;

import com.microservice.user.infrastructure.entities.UserProfileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para perfiles de usuario
 */
@Repository
public interface JpaUserProfileRepository extends JpaRepository<UserProfileEntity, UUID> {
    
    Optional<UserProfileEntity> findByExternalUserId(UUID externalUserId);
    
    Page<UserProfileEntity> findAllByDeletedFalse(Pageable pageable);
    
    boolean existsByExternalUserId(UUID externalUserId);
    
    Optional<UserProfileEntity> findByIdAndDeletedFalse(UUID id);
    
    Optional<UserProfileEntity> findByExternalUserIdAndDeletedFalse(UUID externalUserId);
}
