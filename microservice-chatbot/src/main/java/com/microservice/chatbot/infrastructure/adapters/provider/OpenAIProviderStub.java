package com.microservice.chatbot.infrastructure.adapters.provider;

import com.microservice.chatbot.application.dto.ChatApiResponse;
import com.microservice.chatbot.domain.models.ContextPayload;
import com.microservice.chatbot.domain.models.ModelMetadata;
import com.microservice.chatbot.domain.port.out.IAIProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Stub implementation of IAIProvider for testing.
 * Location: infrastructure/adapters/provider - Test/fallback provider.
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "ai.provider", havingValue = "openai_stub")
public class OpenAIProviderStub implements IAIProvider {

    @Override
    public ChatApiResponse generateResponse(ContextPayload contextPayload) {
        log.info("OpenAI Stub provider generating mock response");

        String userMessage = contextPayload.getUserMessage();
        String mockAnswer = generateMockAnswer(userMessage);

        return ChatApiResponse.builder()
                .answerText(mockAnswer)
                .sources(List.of(
                        ChatApiResponse.SourceRef.builder()
                                .id("stub-source-1")
                                .type("faq")
                                .score(BigDecimal.valueOf(0.95))
                                .build()))
                .actions(List.of(
                        ChatApiResponse.ActionRef.builder()
                                .type("none")
                                .payload(Map.of())
                                .build()))
                .intent(ChatApiResponse.IntentRef.of("general_question", BigDecimal.valueOf(0.85)))
                .build();
    }

    @Override
    public Flux<String> streamResponse(ContextPayload contextPayload) {
        return Flux.just(generateResponse(contextPayload).getAnswerText());
    }

    @Override
    public ModelMetadata getModelMetadata() {
        return ModelMetadata.stub();
    }

    @Override
    public boolean isConfigured() {
        return true; // Stub is always configured
    }

    private String generateMockAnswer(String userMessage) {
        if (userMessage == null || userMessage.isEmpty()) {
            return "Hello! How can I help you today?";
        }

        String lowerMessage = userMessage.toLowerCase();

        if (lowerMessage.contains("hello") || lowerMessage.contains("hi") || lowerMessage.contains("hola")) {
            return "Hello! Welcome to Karibea. How can I assist you today?";
        }

        if (lowerMessage.contains("product") || lowerMessage.contains("producto")) {
            return "I'd be happy to help you find products! What are you looking for today? " +
                    "You can browse our catalog or tell me specifically what you need.";
        }

        if (lowerMessage.contains("order") || lowerMessage.contains("pedido")) {
            return "To check your order status, please provide your order number. " +
                    "I can also help you track your delivery or answer questions about your purchase.";
        }

        if (lowerMessage.contains("help") || lowerMessage.contains("ayuda")) {
            return "I'm here to help! You can ask me about:\n" +
                    "- Product information and availability\n" +
                    "- Order status and tracking\n" +
                    "- Shipping and delivery\n" +
                    "- Returns and refunds\n" +
                    "What would you like to know?";
        }

        return "Thank you for your message. I understand you're asking about: \"" + userMessage + "\". " +
                "Let me help you with that. Could you provide more details about what you need?";
    }
}
