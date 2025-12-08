package com.microservice.identity.infrastructure.adapters;

import com.microservice.identity.domain.models.Role;
import com.microservice.identity.domain.port.out.RoleRepositoryPort;
import com.microservice.identity.infrastructure.entities.RoleEntity;
import com.microservice.identity.infrastructure.repositories.RoleJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * JPA Adapter for RoleRepositoryPort.
 * Implements the repository port using JPA and entity-domain model conversion.
 * Follows hexagonal architecture by adapting infrastructure to domain.
 */
@Component
public class RoleRepositoryAdapter implements RoleRepositoryPort {

    private final RoleJpaRepository roleJpaRepository;

    public RoleRepositoryAdapter(RoleJpaRepository roleJpaRepository) {
        this.roleJpaRepository = roleJpaRepository;
    }

    @Override
    public Optional<Role> findById(UUID id) {
        return roleJpaRepository.findById(id)
                .map(RoleEntity::toDomainModel);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleJpaRepository.findByName(name)
                .map(RoleEntity::toDomainModel);
    }

    @Override
    public List<Role> findAll() {
        return roleJpaRepository.findAll().stream()
                .map(RoleEntity::toDomainModel)
                .toList();
    }

    @Override
    public Role save(Role role) {
        RoleEntity roleEntity = RoleEntity.fromDomainModel(role);
        RoleEntity savedEntity = roleJpaRepository.save(roleEntity);
        return savedEntity.toDomainModel();
    }

    @Override
    public void delete(Role role) {
        RoleEntity roleEntity = RoleEntity.fromDomainModel(role);
        roleJpaRepository.delete(roleEntity);
    }

    @Override
    public boolean existsByName(String name) {
        return roleJpaRepository.existsByName(name);
    }
}
