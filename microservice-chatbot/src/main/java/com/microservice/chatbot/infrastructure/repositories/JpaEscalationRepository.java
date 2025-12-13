package com.microservice.chatbot.infrastructure.repositories;

import com.microservice.chatbot.infrastructure.entities.EscalationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA Repository for EscalationEntity.
 * Location: infrastructure/repositories - JPA repository.
 */
@Repository
public interface JpaEscalationRepository extends JpaRepository<EscalationEntity, UUID> {

    List<EscalationEntity> findByConversationId(UUID conversationId);

    @Query("SELECT e FROM EscalationEntity e WHERE e.resolvedAt IS NULL")
    List<EscalationEntity> findAllPending();

    List<EscalationEntity> findByExternalAssignedAgentId(UUID agentId);
}
