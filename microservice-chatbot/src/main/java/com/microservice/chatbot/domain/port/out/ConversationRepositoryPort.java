package com.microservice.chatbot.domain.port.out;

import com.microservice.chatbot.domain.models.Conversation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port for conversation persistence.
 * Location: domain/port/out - External dependency contract.
 */
public interface ConversationRepositoryPort {

    /**
     * Saves a conversation.
     *
     * @param conversation the conversation to save
     * @return the saved conversation
     */
    Conversation save(Conversation conversation);

    /**
     * Finds a conversation by its ID.
     *
     * @param id the conversation UUID
     * @return optional containing the conversation if found
     */
    Optional<Conversation> findById(UUID id);

    /**
     * Finds all conversations for a user.
     *
     * @param externalUserProfileId the user's external profile ID
     * @return list of conversations for the user
     */
    List<Conversation> findByExternalUserProfileId(UUID externalUserProfileId);

    /**
     * Finds a conversation by session ID.
     *
     * @param externalSessionId the external session ID
     * @return optional containing the conversation if found
     */
    Optional<Conversation> findByExternalSessionId(UUID externalSessionId);

    /**
     * Finds all active conversations.
     *
     * @return list of active (not deleted, not ended) conversations
     */
    List<Conversation> findAllActive();

    /**
     * Soft deletes a conversation.
     *
     * @param id the conversation UUID
     */
    void softDelete(UUID id);

    /**
     * Checks if a conversation exists.
     *
     * @param id the conversation UUID
     * @return true if the conversation exists
     */
    boolean existsById(UUID id);
}
