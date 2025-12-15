package com.microservice.user.infrastructure.repositories;

import com.microservice.user.infrastructure.entities.LanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para idiomas
 */
@Repository
public interface JpaLanguageRepository extends JpaRepository<LanguageEntity, UUID> {
    
    Optional<LanguageEntity> findByCode(String code);
}
