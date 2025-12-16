package com.microservice.chatbot.application.usecases;

import com.microservice.chatbot.application.services.ChatOrchestrationService;
import com.microservice.chatbot.domain.models.Conversation;
import com.microservice.chatbot.domain.port.in.GetConversationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Implementation of GetConversationUseCase.
 * Location: application/usecases - Use case implementation.
 */
@Service
@RequiredArgsConstructor
public class GetConversationUseCaseImpl implements GetConversationUseCase {

    private final ChatOrchestrationService chatOrchestrationService;
    private final com.microservice.chatbot.domain.port.out.ExternalUserServicePort externalUserServicePort;

    @Override
    public Conversation getConversation(UUID conversationId) {
        Conversation conversation = chatOrchestrationService.getConversation(conversationId);

        // Enrich with user info
        if (conversation.getExternalUserProfileId() != null) {
            com.microservice.chatbot.domain.models.UserInfo userInfo = externalUserServicePort
                    .getUserOrFallback(conversation.getExternalUserProfileId());
            conversation.setUserInfo(userInfo);
        }
        return conversation;
    }
}
