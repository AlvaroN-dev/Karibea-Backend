package com.microservice.identity.infrastructure.repositories;

import com.microservice.identity.infrastructure.entities.AccessLevelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA Repository for AccessLevelEntity.
 * Provides database access methods for AccessLevel entities.
 */
@Repository
public interface AccessLevelJpaRepository extends JpaRepository<AccessLevelEntity, UUID> {

    /**
     * Find an access level by name.
     *
     * @param name the access level name
     * @return Optional containing the access level if found
     */
    Optional<AccessLevelEntity> findByName(String name);

    /**
     * Check if an access level exists by name.
     *
     * @param name the access level name
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);
}
