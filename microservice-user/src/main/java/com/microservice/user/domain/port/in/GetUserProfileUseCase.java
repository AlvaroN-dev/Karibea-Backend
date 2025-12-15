package com.microservice.user.domain.port.in;

import com.microservice.user.domain.models.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Puerto entrante - Caso de uso para consultar perfiles de usuario
 */
public interface GetUserProfileUseCase {
    
    Optional<UserProfile> findById(UUID profileId);
    
    Optional<UserProfile> findByExternalUserId(UUID externalUserId);
    
    Page<UserProfile> findAll(Pageable pageable);
    
    boolean existsByExternalUserId(UUID externalUserId);
}
