package com.microservice.chatbot.infrastructure.repositories;

import com.microservice.chatbot.infrastructure.entities.ConversationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA Repository for ConversationEntity.
 * Location: infrastructure/repositories - JPA repository.
 */
@Repository
public interface JpaConversationRepository extends JpaRepository<ConversationEntity, UUID> {

    Optional<ConversationEntity> findByExternalSessionId(UUID externalSessionId);

    List<ConversationEntity> findByExternalUserProfilesId(UUID externalUserProfilesId);

    @Query("SELECT c FROM ConversationEntity c WHERE c.isDeleted = false AND c.endedAt IS NULL")
    List<ConversationEntity> findAllActive();

    @Modifying
    @Query("UPDATE ConversationEntity c SET c.isDeleted = true, c.deletedAt = CURRENT_TIMESTAMP WHERE c.id = :id")
    void softDelete(@Param("id") UUID id);
}
