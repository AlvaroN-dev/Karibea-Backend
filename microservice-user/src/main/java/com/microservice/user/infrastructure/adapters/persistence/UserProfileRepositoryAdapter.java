package com.microservice.user.infrastructure.adapters.persistence;

import com.microservice.user.domain.models.UserProfile;
import com.microservice.user.domain.port.out.UserProfileRepositoryPort;
import com.microservice.user.infrastructure.entities.UserProfileEntity;
import com.microservice.user.infrastructure.entities.mapper.UserProfileEntityMapper;
import com.microservice.user.infrastructure.repositories.JpaUserProfileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador de persistencia para perfiles de usuario
 * Implementa el puerto saliente del dominio
 */
@Component
public class UserProfileRepositoryAdapter implements UserProfileRepositoryPort {
    
    private final JpaUserProfileRepository jpaRepository;
    private final UserProfileEntityMapper mapper;
    
    public UserProfileRepositoryAdapter(JpaUserProfileRepository jpaRepository,
                                        UserProfileEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
    
    @Override
    public UserProfile save(UserProfile profile) {
        UserProfileEntity entity = mapper.toEntity(profile);
        UserProfileEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
    
    @Override
    public Optional<UserProfile> findById(UUID id) {
        return jpaRepository.findByIdAndDeletedFalse(id)
            .map(mapper::toDomain);
    }
    
    @Override
    public Optional<UserProfile> findByExternalUserId(UUID externalUserId) {
        return jpaRepository.findByExternalUserIdAndDeletedFalse(externalUserId)
            .map(mapper::toDomain);
    }
    
    @Override
    public Page<UserProfile> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable)
            .map(mapper::toDomain);
    }
    
    @Override
    public Page<UserProfile> findAllActive(Pageable pageable) {
        return jpaRepository.findAllByDeletedFalse(pageable)
            .map(mapper::toDomain);
    }
    
    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existsByExternalUserId(UUID externalUserId) {
        return jpaRepository.existsByExternalUserId(externalUserId);
    }
}
