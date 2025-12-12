package com.microservice.user.infrastructure.repositories;

import com.microservice.user.infrastructure.entities.GenderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para géneros
 */
@Repository
public interface JpaGenderRepository extends JpaRepository<GenderEntity, UUID> {
    
    Optional<GenderEntity> findByName(String name);
}
