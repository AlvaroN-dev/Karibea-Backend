package com.microservice.identity.infrastructure.repositories;

import com.microservice.identity.infrastructure.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA Repository for RoleEntity.
 * Provides database access methods for Role entities.
 */
@Repository
public interface RoleJpaRepository extends JpaRepository<RoleEntity, UUID> {

    /**
     * Find a role by name.
     *
     * @param name the role name
     * @return Optional containing the role if found
     */
    Optional<RoleEntity> findByName(String name);

    /**
     * Check if a role exists by name.
     *
     * @param name the role name
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);
}
