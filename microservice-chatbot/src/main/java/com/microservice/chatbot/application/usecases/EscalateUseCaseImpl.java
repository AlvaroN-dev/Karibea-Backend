package com.microservice.chatbot.application.usecases;

import com.microservice.chatbot.application.dto.EscalationRequest;
import com.microservice.chatbot.domain.events.EscalationCreatedEvent;
import com.microservice.chatbot.domain.exceptions.ConversationNotFoundException;
import com.microservice.chatbot.domain.models.Escalation;
import com.microservice.chatbot.domain.port.in.EscalateUseCase;
import com.microservice.chatbot.domain.port.out.ConversationRepositoryPort;
import com.microservice.chatbot.domain.port.out.EscalationRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of EscalateUseCase.
 * Location: application/usecases - Use case implementation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EscalateUseCaseImpl implements EscalateUseCase {

    private final ConversationRepositoryPort conversationRepository;
    private final EscalationRepositoryPort escalationRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public Escalation escalate(EscalationRequest request) {
        log.info("Escalating conversation: {}", request.getConversationId());

        // Verify conversation exists
        if (!conversationRepository.existsById(request.getConversationId())) {
            throw new ConversationNotFoundException(request.getConversationId());
        }

        // Create escalation
        Escalation.Priority priority = Escalation.Priority.valueOf(
                request.getPriority().toUpperCase());

        Escalation escalation = Escalation.createEscalation(
                request.getConversationId(),
                request.getReason(),
                priority);

        Escalation saved = escalationRepository.save(escalation);

        // Publish event
        eventPublisher.publishEvent(EscalationCreatedEvent.of(
                saved.getId(),
                saved.getConversationId(),
                saved.getReason(),
                saved.getPriority().name()));

        log.info("Escalation created: {}", saved.getId());

        return saved;
    }
}
