package com.microservice.chatbot.infrastructure.repositories;

import com.microservice.chatbot.domain.models.Conversation;
import com.microservice.chatbot.domain.port.out.ConversationRepositoryPort;
import com.microservice.chatbot.infrastructure.entities.ConversationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter implementing ConversationRepositoryPort.
 * Location: infrastructure/repositories - Port adapter.
 */
@Component
@RequiredArgsConstructor
public class ConversationRepositoryAdapter implements ConversationRepositoryPort {

    private final JpaConversationRepository jpaRepository;

    @Override
    @Transactional
    public Conversation save(Conversation conversation) {
        ConversationEntity entity = toEntity(conversation);
        ConversationEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Conversation> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Conversation> findByExternalUserProfileId(UUID externalUserProfileId) {
        return jpaRepository.findByExternalUserProfilesId(externalUserProfileId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Conversation> findByExternalSessionId(UUID externalSessionId) {
        return jpaRepository.findByExternalSessionId(externalSessionId).map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Conversation> findAllActive() {
        return jpaRepository.findAllActive()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void softDelete(UUID id) {
        jpaRepository.softDelete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    private ConversationEntity toEntity(Conversation domain) {
        return ConversationEntity.builder()
                .id(domain.getId())
                .externalUserProfilesId(domain.getExternalUserProfileId())
                .externalSessionId(domain.getExternalSessionId())
                .statuConversationId(domain.getStatusId())
                .channel(domain.getChannel())
                .startedAt(domain.getStartedAt())
                .endedAt(domain.getEndedAt())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .deletedAt(domain.getDeletedAt())
                .isDeleted(domain.isDeleted())
                .build();
    }

    private Conversation toDomain(ConversationEntity entity) {
        return Conversation.builder()
                .id(entity.getId())
                .externalUserProfileId(entity.getExternalUserProfilesId())
                .externalSessionId(entity.getExternalSessionId())
                .statusId(entity.getStatuConversationId())
                .channel(entity.getChannel())
                .startedAt(entity.getStartedAt())
                .endedAt(entity.getEndedAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .deleted(entity.getIsDeleted() != null && entity.getIsDeleted())
                .build();
    }
}
