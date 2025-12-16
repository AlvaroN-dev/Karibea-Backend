package com.microservice.chatbot.domain.port.out;

import com.microservice.chatbot.domain.models.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port for message persistence.
 * Location: domain/port/out - External dependency contract.
 */
public interface MessageRepositoryPort {

    /**
     * Saves a message.
     *
     * @param message the message to save
     * @return the saved message
     */
    Message save(Message message);

    /**
     * Finds a message by its ID.
     *
     * @param id the message UUID
     * @return optional containing the message if found
     */
    Optional<Message> findById(UUID id);

    /**
     * Finds all messages for a conversation.
     *
     * @param conversationId the conversation UUID
     * @return list of messages ordered by creation time
     */
    List<Message> findByConversationId(UUID conversationId);

    /**
     * Finds the last N messages for a conversation.
     *
     * @param conversationId the conversation UUID
     * @param limit          maximum number of messages to return
     * @return list of the most recent messages
     */
    List<Message> findLastMessagesByConversationId(UUID conversationId, int limit);

    /**
     * Counts messages in a conversation.
     *
     * @param conversationId the conversation UUID
     * @return the number of messages
     */
    long countByConversationId(UUID conversationId);

    /**
     * Marks all messages in a conversation as read.
     *
     * @param conversationId the conversation UUID
     */
    void markAllAsRead(UUID conversationId);
}
