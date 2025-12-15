package com.microservice.shopcart.infrastructure.repositories;

import com.microservice.shopcart.infrastructure.entities.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for StatusEntity.
 */
@Repository
public interface JpaStatusRepository extends JpaRepository<StatusEntity, Long> {
    
    Optional<StatusEntity> findByName(String name);
}
