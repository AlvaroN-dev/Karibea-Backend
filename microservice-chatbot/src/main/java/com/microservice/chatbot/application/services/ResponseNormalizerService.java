package com.microservice.chatbot.application.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.chatbot.application.dto.ChatApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service for normalizing AI model responses to standard format.
 * Location: application/services - Response processing service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResponseNormalizerService {

    private final ObjectMapper objectMapper;

    /**
     * Normalizes the raw AI response to ChatApiResponse format.
     *
     * @param rawResponse the raw JSON string from AI
     * @return normalized ChatApiResponse
     */
    public ChatApiResponse normalize(String rawResponse) {
        if (rawResponse == null || rawResponse.isEmpty()) {
            return ChatApiResponse.simpleResponse("I couldn't generate a response. Please try again.");
        }

        try {
            // Try to parse as JSON
            JsonNode root = objectMapper.readTree(extractJson(rawResponse));

            String answerText = extractString(root, "answer_text", rawResponse);
            List<ChatApiResponse.SourceRef> sources = extractSources(root);
            List<ChatApiResponse.ActionRef> actions = extractActions(root);
            ChatApiResponse.IntentRef intent = extractIntent(root);

            return ChatApiResponse.builder()
                    .answerText(answerText)
                    .sources(sources)
                    .actions(actions)
                    .intent(intent)
                    .build();

        } catch (JsonProcessingException e) {
            log.warn("Failed to parse AI response as JSON, using raw text: {}", e.getMessage());
            return ChatApiResponse.simpleResponse(rawResponse);
        }
    }

    /**
     * Extracts JSON from a response that might contain markdown code blocks.
     */
    private String extractJson(String response) {
        // Remove markdown code blocks if present
        String cleaned = response.trim();
        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7);
        } else if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3);
        }
        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.length() - 3);
        }
        return cleaned.trim();
    }

    /**
     * Extracts a string field from JSON.
     */
    private String extractString(JsonNode root, String field, String defaultValue) {
        JsonNode node = root.get(field);
        return (node != null && !node.isNull()) ? node.asText() : defaultValue;
    }

    /**
     * Extracts source references from JSON.
     */
    private List<ChatApiResponse.SourceRef> extractSources(JsonNode root) {
        List<ChatApiResponse.SourceRef> sources = new ArrayList<>();
        JsonNode sourcesNode = root.get("sources");

        if (sourcesNode != null && sourcesNode.isArray()) {
            for (JsonNode sourceNode : sourcesNode) {
                sources.add(ChatApiResponse.SourceRef.builder()
                        .id(extractString(sourceNode, "id", ""))
                        .type(extractString(sourceNode, "type", "unknown"))
                        .score(extractDecimal(sourceNode, "score"))
                        .build());
            }
        }

        return sources;
    }

    /**
     * Extracts action references from JSON.
     */
    private List<ChatApiResponse.ActionRef> extractActions(JsonNode root) {
        List<ChatApiResponse.ActionRef> actions = new ArrayList<>();
        JsonNode actionsNode = root.get("actions");

        if (actionsNode != null && actionsNode.isArray()) {
            for (JsonNode actionNode : actionsNode) {
                Map<String, Object> payload = Map.of();
                JsonNode payloadNode = actionNode.get("payload");
                if (payloadNode != null && !payloadNode.isNull()) {
                    try {
                        payload = objectMapper.convertValue(payloadNode,
                                new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {
                                });
                    } catch (Exception e) {
                        log.warn("Failed to parse action payload: {}", e.getMessage());
                    }
                }

                actions.add(ChatApiResponse.ActionRef.builder()
                        .type(extractString(actionNode, "type", "none"))
                        .payload(payload)
                        .build());
            }
        }

        if (actions.isEmpty()) {
            actions.add(ChatApiResponse.ActionRef.builder()
                    .type("none")
                    .payload(Map.of())
                    .build());
        }

        return actions;
    }

    /**
     * Extracts intent reference from JSON.
     */
    private ChatApiResponse.IntentRef extractIntent(JsonNode root) {
        JsonNode intentNode = root.get("intent");

        if (intentNode != null && !intentNode.isNull()) {
            return ChatApiResponse.IntentRef.builder()
                    .id(extractString(intentNode, "id", "unknown"))
                    .confidence(extractDecimal(intentNode, "confidence"))
                    .build();
        }

        return ChatApiResponse.IntentRef.of("unknown", BigDecimal.valueOf(0.5));
    }

    /**
     * Extracts a decimal field from JSON.
     */
    private BigDecimal extractDecimal(JsonNode node, String field) {
        JsonNode fieldNode = node.get(field);
        if (fieldNode != null && !fieldNode.isNull()) {
            return BigDecimal.valueOf(fieldNode.asDouble());
        }
        return BigDecimal.valueOf(0.5);
    }
}
