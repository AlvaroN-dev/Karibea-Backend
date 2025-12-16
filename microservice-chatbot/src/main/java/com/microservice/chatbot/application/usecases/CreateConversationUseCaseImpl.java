package com.microservice.chatbot.application.usecases;

import com.microservice.chatbot.application.dto.ConversationRequest;
import com.microservice.chatbot.domain.models.Conversation;
import com.microservice.chatbot.domain.port.in.CreateConversationUseCase;
import com.microservice.chatbot.domain.port.out.ConversationRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Implementation of CreateConversationUseCase.
 * Location: application/usecases - Use case implementation.
 */
@Service
@RequiredArgsConstructor
public class CreateConversationUseCaseImpl implements CreateConversationUseCase {

    private final ConversationRepositoryPort conversationRepository;

    @Override
    @Transactional
    public Conversation createConversation(ConversationRequest request) {
        Conversation conversation = Conversation.builder()
                .id(UUID.randomUUID())
                .externalUserProfileId(request.getExternalUserProfileId())
                .externalSessionId(
                        request.getExternalSessionId() != null ? request.getExternalSessionId() : UUID.randomUUID())
                .channel(request.getChannel())
                .startedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .deleted(false)
                .build();

        return conversationRepository.save(conversation);
    }
}
