package com.microservice.chatbot.application.services;

import com.microservice.chatbot.application.dto.ChatApiResponse;
import com.microservice.chatbot.application.dto.MessageRequest;
import com.microservice.chatbot.domain.events.MessageSentEvent;
import com.microservice.chatbot.domain.exceptions.ConversationNotFoundException;
import com.microservice.chatbot.domain.models.Conversation;
import com.microservice.chatbot.domain.models.ContextPayload;
import com.microservice.chatbot.domain.models.Message;
import com.microservice.chatbot.domain.port.out.ConversationRepositoryPort;
import com.microservice.chatbot.domain.port.out.IAIProvider;
import com.microservice.chatbot.domain.port.out.MessageRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Main orchestration service for the RAG chatbot flow.
 * Location: application/services - Core orchestration service.
 * 
 * Flow: User → API → ChatService → Persistencia → Retriever →
 * Construcción del Prompt → IAIProvider → Normalizador → Guardado → Respuesta
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatOrchestrationService {

        private final ConversationRepositoryPort conversationRepository;
        private final MessageRepositoryPort messageRepository;
        private final ContextBuilderService contextBuilderService;
        private final IAIProvider aiProvider;
        private final ApplicationEventPublisher eventPublisher;

        /**
         * Processes a user message through the complete RAG flow.
         */
        @Transactional
        public ChatApiResponse processMessage(MessageRequest request) {
                UUID conversationId = request.getConversationId();

                log.info("Processing message for conversation: {}", conversationId);

                // 1. Validate conversation exists
                conversationRepository.findById(conversationId)
                                .orElseThrow(() -> new ConversationNotFoundException(conversationId));

                // 2. Save user message
                Message userMessage = Message.createUserMessage(
                                conversationId,
                                request.getExternalSenderId(),
                                request.getContent());
                Message savedUserMessage = messageRepository.save(userMessage);

                // Publish event
                eventPublisher.publishEvent(MessageSentEvent.of(
                                savedUserMessage.getId(),
                                conversationId,
                                "USER",
                                request.getContent()));

                // 3. Get conversation history
                List<Message> history = messageRepository.findLastMessagesByConversationId(conversationId, 10);

                // 4. Build context with retriever
                ContextPayload contextPayload = contextBuilderService.buildContext(
                                request.getContent(),
                                history);

                // 5. Generate AI response
                ChatApiResponse aiResponse;
                try {
                        aiResponse = aiProvider.generateResponse(contextPayload);
                } catch (Exception e) {
                        log.error("AI provider error: {}", e.getMessage(), e);
                        aiResponse = ChatApiResponse.simpleResponse(
                                        "I'm sorry, I couldn't process your request at the moment. Please try again later.");
                }

                // 6. Save assistant message
                Message assistantMessage = Message.createAssistantMessage(
                                conversationId,
                                aiResponse.getAnswerText(),
                                aiResponse.getIntent() != null ? aiResponse.getIntent().getId() : null,
                                aiResponse.getIntent() != null ? aiResponse.getIntent().getConfidence()
                                                : BigDecimal.ZERO);
                messageRepository.save(assistantMessage);

                // Publish event
                eventPublisher.publishEvent(MessageSentEvent.of(
                                assistantMessage.getId(),
                                conversationId,
                                "ASSISTANT",
                                aiResponse.getAnswerText()));

                log.info("Message processed successfully for conversation: {}", conversationId);

                return aiResponse;
        }

        /**
         * Gets conversation with messages.
         */
        @Transactional(readOnly = true)
        public Conversation getConversation(UUID conversationId) {
                Conversation conversation = conversationRepository.findById(conversationId)
                                .orElseThrow(() -> new ConversationNotFoundException(conversationId));

                List<Message> messages = messageRepository.findByConversationId(conversationId);

                return Conversation.builder()
                                .id(conversation.getId())
                                .externalUserProfileId(conversation.getExternalUserProfileId())
                                .externalSessionId(conversation.getExternalSessionId())
                                .statusId(conversation.getStatusId())
                                .channel(conversation.getChannel())
                                .startedAt(conversation.getStartedAt())
                                .endedAt(conversation.getEndedAt())
                                .createdAt(conversation.getCreatedAt())
                                .updatedAt(conversation.getUpdatedAt())
                                .messages(messages)
                                .build();
        }
}
