package com.microservice.chatbot.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for escalation creation.
 * Location: application/dto - Response DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EscalationResponse {

    private UUID id;
    private UUID conversationId;
    private String reason;
    private String priority;
    private LocalDateTime escalatedAt;
}
