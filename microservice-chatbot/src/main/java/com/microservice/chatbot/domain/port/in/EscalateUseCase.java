package com.microservice.chatbot.domain.port.in;

import com.microservice.chatbot.application.dto.EscalationRequest;
import com.microservice.chatbot.domain.models.Escalation;

/**
 * Input port for escalating conversations to human agents.
 * Location: domain/port/in - Defines the use case contract.
 */
public interface EscalateUseCase {

    /**
     * Escalates a conversation to a human agent.
     *
     * @param request the escalation request with conversation ID and reason
     * @return the created escalation
     */
    Escalation escalate(EscalationRequest request);
}
