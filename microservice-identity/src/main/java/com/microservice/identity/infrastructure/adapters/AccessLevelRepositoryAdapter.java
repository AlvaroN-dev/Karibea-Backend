package com.microservice.identity.infrastructure.adapters;

import com.microservice.identity.domain.models.AccessLevel;
import com.microservice.identity.domain.port.out.AccessLevelRepositoryPort;
import com.microservice.identity.infrastructure.entities.AccessLevelEntity;
import com.microservice.identity.infrastructure.repositories.AccessLevelJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JPA Adapter for AccessLevelRepositoryPort.
 * Implements the repository port using JPA and entity-domain model conversion.
 * Follows hexagonal architecture by adapting infrastructure to domain.
 */
@Component
public class AccessLevelRepositoryAdapter implements AccessLevelRepositoryPort {

    private final AccessLevelJpaRepository accessLevelJpaRepository;

    public AccessLevelRepositoryAdapter(AccessLevelJpaRepository accessLevelJpaRepository) {
        this.accessLevelJpaRepository = accessLevelJpaRepository;
    }

    @Override
    public Optional<AccessLevel> findById(UUID id) {
        return accessLevelJpaRepository.findById(id)
                .map(AccessLevelEntity::toDomainModel);
    }

    @Override
    public Optional<AccessLevel> findByName(String name) {
        return accessLevelJpaRepository.findByName(name)
                .map(AccessLevelEntity::toDomainModel);
    }

    @Override
    public List<AccessLevel> findAll() {
        return accessLevelJpaRepository.findAll().stream()
                .map(AccessLevelEntity::toDomainModel)
                .toList();
    }

    @Override
    public AccessLevel save(AccessLevel accessLevel) {
        AccessLevelEntity entity = AccessLevelEntity.fromDomainModel(accessLevel);
        AccessLevelEntity savedEntity = accessLevelJpaRepository.save(entity);
        return savedEntity.toDomainModel();
    }

    @Override
    public void delete(AccessLevel accessLevel) {
        AccessLevelEntity entity = AccessLevelEntity.fromDomainModel(accessLevel);
        accessLevelJpaRepository.delete(entity);
    }
}
