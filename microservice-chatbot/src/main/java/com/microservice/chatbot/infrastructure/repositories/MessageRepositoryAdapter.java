package com.microservice.chatbot.infrastructure.repositories;

import com.microservice.chatbot.domain.models.Message;
import com.microservice.chatbot.domain.port.out.MessageRepositoryPort;
import com.microservice.chatbot.infrastructure.entities.MessageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter implementing MessageRepositoryPort.
 * Location: infrastructure/repositories - Port adapter.
 */
@Component
@RequiredArgsConstructor
public class MessageRepositoryAdapter implements MessageRepositoryPort {

    private final JpaMessageRepository jpaRepository;

    @Override
    @Transactional
    public Message save(Message message) {
        MessageEntity entity = toEntity(message);
        MessageEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Message> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> findByConversationId(UUID conversationId) {
        return jpaRepository.findByConversationIdOrderByCreatedAtAsc(conversationId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> findLastMessagesByConversationId(UUID conversationId, int limit) {
        return jpaRepository.findLastMessagesByConversationId(conversationId, limit)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long countByConversationId(UUID conversationId) {
        return jpaRepository.countByConversationId(conversationId);
    }

    @Override
    @Transactional
    public void markAllAsRead(UUID conversationId) {
        jpaRepository.markAllAsRead(conversationId);
    }

    private MessageEntity toEntity(Message domain) {
        return MessageEntity.builder()
                .id(domain.getId())
                .conversationId(domain.getConversationId())
                .senderType(domain.getSenderType() != null ? domain.getSenderType().name() : null)
                .externalSenderId(domain.getExternalSenderId())
                .messageTypeId(domain.getMessageTypeId())
                .content(domain.getContent())
                .metadata(domain.getMetadata())
                .intent(domain.getIntent())
                .confidence(domain.getConfidence())
                .isRead(domain.isRead())
                .createdAt(domain.getCreatedAt())
                .build();
    }

    private Message toDomain(MessageEntity entity) {
        return Message.builder()
                .id(entity.getId())
                .conversationId(entity.getConversationId())
                .senderType(parseSenderType(entity.getSenderType()))
                .externalSenderId(entity.getExternalSenderId())
                .messageTypeId(entity.getMessageTypeId())
                .content(entity.getContent())
                .metadata(entity.getMetadata())
                .intent(entity.getIntent())
                .confidence(entity.getConfidence())
                .read(entity.getIsRead() != null && entity.getIsRead())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    private Message.SenderType parseSenderType(String type) {
        if (type == null)
            return null;
        try {
            return Message.SenderType.valueOf(type);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
