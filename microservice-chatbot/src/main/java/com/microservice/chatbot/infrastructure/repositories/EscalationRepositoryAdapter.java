package com.microservice.chatbot.infrastructure.repositories;

import com.microservice.chatbot.domain.models.Escalation;
import com.microservice.chatbot.domain.port.out.EscalationRepositoryPort;
import com.microservice.chatbot.infrastructure.entities.EscalationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter implementing EscalationRepositoryPort.
 * Location: infrastructure/repositories - Port adapter.
 */
@Component
@RequiredArgsConstructor
public class EscalationRepositoryAdapter implements EscalationRepositoryPort {

    private final JpaEscalationRepository jpaRepository;

    @Override
    @Transactional
    public Escalation save(Escalation escalation) {
        EscalationEntity entity = toEntity(escalation);
        EscalationEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Escalation> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Escalation> findByConversationId(UUID conversationId) {
        return jpaRepository.findByConversationId(conversationId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Escalation> findAllPending() {
        return jpaRepository.findAllPending()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Escalation> findByExternalAssignedAgentId(UUID agentId) {
        return jpaRepository.findByExternalAssignedAgentId(agentId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private EscalationEntity toEntity(Escalation domain) {
        return EscalationEntity.builder()
                .id(domain.getId())
                .conversationId(domain.getConversationId())
                .reason(domain.getReason())
                .priority(domain.getPriority() != null ? domain.getPriority().name() : null)
                .externalAssignedAgentId(domain.getExternalAssignedAgentId())
                .statuEscalationId(domain.getStatusId())
                .escalatedAt(domain.getEscalatedAt())
                .resolvedAt(domain.getResolvedAt())
                .build();
    }

    private Escalation toDomain(EscalationEntity entity) {
        return Escalation.builder()
                .id(entity.getId())
                .conversationId(entity.getConversationId())
                .reason(entity.getReason())
                .priority(parsePriority(entity.getPriority()))
                .externalAssignedAgentId(entity.getExternalAssignedAgentId())
                .statusId(entity.getStatuEscalationId())
                .escalatedAt(entity.getEscalatedAt())
                .resolvedAt(entity.getResolvedAt())
                .build();
    }

    private Escalation.Priority parsePriority(String priority) {
        if (priority == null)
            return Escalation.Priority.MEDIUM;
        try {
            return Escalation.Priority.valueOf(priority);
        } catch (IllegalArgumentException e) {
            return Escalation.Priority.MEDIUM;
        }
    }
}
