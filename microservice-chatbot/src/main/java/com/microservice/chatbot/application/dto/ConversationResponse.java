package com.microservice.chatbot.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for conversation details.
 * Location: application/dto - Response DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationResponse {

    private UUID id;
    private UUID externalUserProfileId;
    private UUID externalSessionId;
    private String channel;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private LocalDateTime createdAt;
    private List<MessageResponse> messages;
    private int messageCount;

    /**
     * Message response nested DTO.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageResponse {
        private UUID id;
        private String senderType;
        private String content;
        private String intent;
        private Double confidence;
        private boolean read;
        private LocalDateTime createdAt;
    }
}
