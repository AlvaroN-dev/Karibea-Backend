package com.microservice.user.infrastructure.adapters.persistence;

import com.microservice.user.domain.models.UserPreferences;
import com.microservice.user.domain.port.out.PreferencesRepositoryPort;
import com.microservice.user.infrastructure.entities.UserPreferencesEntity;
import com.microservice.user.infrastructure.entities.mapper.PreferencesEntityMapper;
import com.microservice.user.infrastructure.repositories.JpaPreferencesRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador de persistencia para preferencias del usuario
 */
@Component
public class PreferencesRepositoryAdapter implements PreferencesRepositoryPort {
    
    private final JpaPreferencesRepository jpaRepository;
    private final PreferencesEntityMapper mapper;
    
    public PreferencesRepositoryAdapter(JpaPreferencesRepository jpaRepository,
                                        PreferencesEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
    
    @Override
    public UserPreferences save(UserPreferences preferences) {
        UserPreferencesEntity entity = mapper.toEntity(preferences);
        UserPreferencesEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
    
    @Override
    public Optional<UserPreferences> findByExternalUserId(UUID externalUserId) {
        return jpaRepository.findByExternalUserIdAndDeletedFalse(externalUserId)
            .map(mapper::toDomain);
    }
    
    @Override
    @Transactional
    public void deleteByExternalUserId(UUID externalUserId) {
        jpaRepository.deleteByExternalUserId(externalUserId);
    }
    
    @Override
    public boolean existsByExternalUserId(UUID externalUserId) {
        return jpaRepository.existsByExternalUserId(externalUserId);
    }
}
