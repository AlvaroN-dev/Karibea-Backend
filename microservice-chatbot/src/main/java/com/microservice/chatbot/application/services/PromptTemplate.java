package com.microservice.chatbot.application.services;

import com.microservice.chatbot.domain.models.ChatSource;
import com.microservice.chatbot.domain.models.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * PromptTemplate for building AI prompts.
 * Contains SYSTEM, CONTEXT, HISTORY, USER, and OUTPUT FORMAT sections.
 * Location: application/services - Orchestration component.
 */
@Component
public class PromptTemplate {

    // ========================= SYSTEM SECTION =========================
    private static final String SYSTEM_TEMPLATE = """
            You are Karibea Assistant, an intelligent and friendly virtual assistant for the Karibea e-commerce platform.

            YOUR ROLE:
            - Help customers find products, answer questions about orders, and provide excellent customer service
            - Be concise, helpful, and professional in all responses
            - Always prioritize accurate information from the provided context
            - If you don't have enough information, acknowledge it and offer to escalate to a human agent

            GUIDELINES:
            - Respond in the same language as the user's message
            - Never invent product information - only use data from the CONTEXT section
            - For order-related queries, always verify with context data
            - Be empathetic with customer concerns
            - Keep responses under 150 words unless detailed explanation is needed

            RESTRICTIONS:
            - Never share internal system details or technical information
            - Never process payments or modify orders directly
            - Never share personal data of other customers
            - Always recommend escalation for complex issues
            """;

    // ========================= CONTEXT SECTION =========================
    private static final String CONTEXT_TEMPLATE = """

            ### RELEVANT CONTEXT ###
            The following information is retrieved from Karibea's database and is relevant to this conversation:

            %s

            Use this context to provide accurate and helpful responses. If the context doesn't contain
            the information needed, acknowledge this limitation.
            ### END CONTEXT ###
            """;

    // ========================= HISTORY SECTION =========================
    private static final String HISTORY_TEMPLATE = """

            ### CONVERSATION HISTORY ###
            Previous messages in this conversation:

            %s

            Continue the conversation naturally, maintaining context from previous exchanges.
            ### END HISTORY ###
            """;

    // ========================= USER MESSAGE SECTION =========================
    private static final String USER_TEMPLATE = """

            ### CURRENT USER MESSAGE ###
            User says: %s
            ### END USER MESSAGE ###
            """;

    // ========================= OUTPUT FORMAT SECTION =========================
    private static final String OUTPUT_FORMAT_TEMPLATE = """

            ### REQUIRED OUTPUT FORMAT ###
            You MUST respond with a valid JSON object in the following exact format:

            {
                "answer_text": "Your helpful response to the user in natural language",
                "sources": [
                    {
                        "id": "source_id_from_context",
                        "type": "product|order|faq|policy",
                        "score": 0.95
                    }
                ],
                "actions": [
                    {
                        "type": "open_url|add_to_cart|escalate|none",
                        "payload": {
                            "url": "optional_url",
                            "product_id": "optional_product_id"
                        }
                    }
                ],
                "intent": {
                    "id": "detected_intent_name",
                    "confidence": 0.87
                }
            }

            IMPORTANT:
            - answer_text: Required. Your natural language response to the user
            - sources: List sources from context used in your response. Empty array if none
            - actions: Suggested actions. Use type:"none" with empty payload if no action needed
            - intent: Detected user intent with confidence score (0.0 to 1.0)

            Common intents: product_search, order_status, price_inquiry, shipping_info,
                           return_request, complaint, general_question, greeting, farewell
            ### END OUTPUT FORMAT ###
            """;

    /**
     * Builds the complete prompt with all sections.
     */
    public String buildPrompt(String userMessage, List<Message> history, List<ChatSource> sources) {
        StringBuilder prompt = new StringBuilder();

        // System section
        prompt.append(SYSTEM_TEMPLATE);

        // Context section
        if (sources != null && !sources.isEmpty()) {
            String contextContent = formatSources(sources);
            prompt.append(String.format(CONTEXT_TEMPLATE, contextContent));
        }

        // History section
        if (history != null && !history.isEmpty()) {
            String historyContent = formatHistory(history);
            prompt.append(String.format(HISTORY_TEMPLATE, historyContent));
        }

        // User message section
        prompt.append(String.format(USER_TEMPLATE, userMessage));

        // Output format section
        prompt.append(OUTPUT_FORMAT_TEMPLATE);

        return prompt.toString();
    }

    /**
     * Gets the system prompt only.
     */
    public String getSystemPrompt() {
        return SYSTEM_TEMPLATE;
    }

    /**
     * Gets the output format instructions.
     */
    public String getOutputFormat() {
        return OUTPUT_FORMAT_TEMPLATE;
    }

    /**
     * Formats the sources for the context section.
     */
    private String formatSources(List<ChatSource> sources) {
        return sources.stream()
                .map(source -> String.format("[%s] %s (ID: %s, Score: %.2f)",
                        source.getType().name(),
                        source.getContent(),
                        source.getId(),
                        source.getScore()))
                .collect(Collectors.joining("\n"));
    }

    /**
     * Formats the message history.
     */
    private String formatHistory(List<Message> history) {
        return history.stream()
                .map(msg -> String.format("%s: %s",
                        msg.getSenderType().name(),
                        msg.getContent()))
                .collect(Collectors.joining("\n"));
    }
}
