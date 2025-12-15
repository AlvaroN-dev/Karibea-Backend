package com.microservice.user.domain.port.out;

import com.microservice.user.domain.models.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Puerto saliente - Repositorio de perfiles de usuario
 */
public interface UserProfileRepositoryPort {
    
    UserProfile save(UserProfile profile);
    
    Optional<UserProfile> findById(UUID id);
    
    Optional<UserProfile> findByExternalUserId(UUID externalUserId);
    
    Page<UserProfile> findAll(Pageable pageable);
    
    Page<UserProfile> findAllActive(Pageable pageable);
    
    void deleteById(UUID id);
    
    boolean existsByExternalUserId(UUID externalUserId);
}
