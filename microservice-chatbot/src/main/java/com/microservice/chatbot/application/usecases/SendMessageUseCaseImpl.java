package com.microservice.chatbot.application.usecases;

import com.microservice.chatbot.application.dto.ChatApiResponse;
import com.microservice.chatbot.application.dto.MessageRequest;
import com.microservice.chatbot.application.services.ChatOrchestrationService;
import com.microservice.chatbot.domain.port.in.SendMessageUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of SendMessageUseCase.
 * Location: application/usecases - Use case implementation.
 */
@Service
@RequiredArgsConstructor
public class SendMessageUseCaseImpl implements SendMessageUseCase {

    private final ChatOrchestrationService chatOrchestrationService;

    @Override
    public ChatApiResponse sendMessage(MessageRequest request) {
        return chatOrchestrationService.processMessage(request);
    }
}
