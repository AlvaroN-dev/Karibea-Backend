package com.microservice.chatbot.domain.port.out;

import com.microservice.chatbot.domain.models.Escalation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port for escalation persistence.
 * Location: domain/port/out - External dependency contract.
 */
public interface EscalationRepositoryPort {

    /**
     * Saves an escalation.
     *
     * @param escalation the escalation to save
     * @return the saved escalation
     */
    Escalation save(Escalation escalation);

    /**
     * Finds an escalation by its ID.
     *
     * @param id the escalation UUID
     * @return optional containing the escalation if found
     */
    Optional<Escalation> findById(UUID id);

    /**
     * Finds all escalations for a conversation.
     *
     * @param conversationId the conversation UUID
     * @return list of escalations for the conversation
     */
    List<Escalation> findByConversationId(UUID conversationId);

    /**
     * Finds all pending escalations.
     *
     * @return list of unresolved escalations
     */
    List<Escalation> findAllPending();

    /**
     * Finds escalations assigned to a specific agent.
     *
     * @param agentId the agent's external ID
     * @return list of escalations assigned to the agent
     */
    List<Escalation> findByExternalAssignedAgentId(UUID agentId);
}
