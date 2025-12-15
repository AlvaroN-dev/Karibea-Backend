package com.microservice.chatbot.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Domain model representing a chat conversation.
 * Contains business logic and validation rules.
 * Location: domain/models - Pure domain entity, no JPA annotations.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {

    private UUID id;
    private UUID externalUserProfileId;
    private UUID externalSessionId;
    private UUID statusId;
    private String channel;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private boolean deleted;

    // Enriched data (transient)
    private UserInfo userInfo;

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Builder.Default
    private List<Message> messages = new ArrayList<>();

    /**
     * Adds a message to the conversation.
     * Business rule: Messages can only be added to active conversations.
     */
    public void addMessage(Message message) {
        if (this.deleted) {
            throw new IllegalStateException("Cannot add message to deleted conversation");
        }
        if (this.endedAt != null) {
            throw new IllegalStateException("Cannot add message to ended conversation");
        }
        this.messages.add(message);
    }

    /**
     * Ends the conversation.
     * Business rule: Only active conversations can be ended.
     */
    public void endConversation() {
        if (this.endedAt != null) {
            throw new IllegalStateException("Conversation is already ended");
        }
        this.endedAt = LocalDateTime.now();
    }

    /**
     * Soft deletes the conversation.
     */
    public void softDelete() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    /**
     * Checks if the conversation is active.
     */
    public boolean isActive() {
        return !this.deleted && this.endedAt == null;
    }

    /**
     * Gets the last N messages for context building.
     */
    public List<Message> getLastMessages(int count) {
        if (messages.isEmpty()) {
            return new ArrayList<>();
        }
        int fromIndex = Math.max(0, messages.size() - count);
        return new ArrayList<>(messages.subList(fromIndex, messages.size()));
    }
}
