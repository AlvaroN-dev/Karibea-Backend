package com.microservice.chatbot.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO for sending a message to the chatbot.
 * Location: application/dto - Request DTO with validation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {

    @NotNull(message = "Conversation ID is required")
    private UUID conversationId;

    @NotBlank(message = "Message content cannot be empty")
    @Size(max = 4000, message = "Message content cannot exceed 4000 characters")
    private String content;

    private UUID externalSenderId;

    private UUID messageTypeId;
}
