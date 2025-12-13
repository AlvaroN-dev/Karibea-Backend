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
 * DTO for escalating a conversation to a human agent.
 * Location: application/dto - Request DTO with validation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EscalationRequest {

    @NotNull(message = "Conversation ID is required")
    private UUID conversationId;

    @NotBlank(message = "Reason is required")
    @Size(max = 255, message = "Reason cannot exceed 255 characters")
    private String reason;

    @NotBlank(message = "Priority is required")
    private String priority;
}
