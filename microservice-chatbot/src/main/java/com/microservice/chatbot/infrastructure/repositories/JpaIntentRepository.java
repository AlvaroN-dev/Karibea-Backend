package com.microservice.chatbot.infrastructure.repositories;

import com.microservice.chatbot.infrastructure.entities.IntentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA Repository for IntentEntity.
 * Location: infrastructure/repositories - JPA repository.
 */
@Repository
public interface JpaIntentRepository extends JpaRepository<IntentEntity, UUID> {

    Optional<IntentEntity> findByName(String name);

    @Query("SELECT i FROM IntentEntity i WHERE i.isActive = true")
    List<IntentEntity> findAllActive();
}
