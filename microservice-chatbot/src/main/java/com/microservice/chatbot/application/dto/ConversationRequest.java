package com.microservice.chatbot.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO for creating a new conversation.
 * Location: application/dto - Request DTO with validation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationRequest {

    private UUID externalUserProfileId;

    private UUID externalSessionId;

    @NotBlank(message = "Channel is required")
    @Size(max = 50, message = "Channel cannot exceed 50 characters")
    private String channel;

    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;
}
