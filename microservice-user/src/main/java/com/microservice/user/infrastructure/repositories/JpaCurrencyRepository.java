package com.microservice.user.infrastructure.repositories;

import com.microservice.user.infrastructure.entities.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para monedas
 */
@Repository
public interface JpaCurrencyRepository extends JpaRepository<CurrencyEntity, UUID> {
    
    Optional<CurrencyEntity> findByCode(String code);
}
