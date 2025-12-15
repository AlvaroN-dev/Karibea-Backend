package com.microservice.chatbot.infrastructure.repositories;

import com.microservice.chatbot.infrastructure.entities.MessageEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA Repository for MessageEntity.
 * Location: infrastructure/repositories - JPA repository.
 */
@Repository
public interface JpaMessageRepository extends JpaRepository<MessageEntity, UUID> {

    List<MessageEntity> findByConversationIdOrderByCreatedAtAsc(UUID conversationId);

    @Query("SELECT m FROM MessageEntity m WHERE m.conversationId = :conversationId ORDER BY m.createdAt DESC")
    List<MessageEntity> findLastMessages(@Param("conversationId") UUID conversationId, Pageable pageable);

    default List<MessageEntity> findLastMessagesByConversationId(UUID conversationId, int limit) {
        return findLastMessages(conversationId, PageRequest.of(0, limit));
    }

    long countByConversationId(UUID conversationId);

    @Modifying
    @Query("UPDATE MessageEntity m SET m.isRead = true WHERE m.conversationId = :conversationId")
    void markAllAsRead(@Param("conversationId") UUID conversationId);
}
