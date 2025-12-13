package com.microservice.chatbot.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Response DTO for source information.
 * Location: application/dto - Response DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SourceResponse {

    private String id;
    private String type;
    private String content;
    private BigDecimal score;
}
