package com.microservice.notification.infrastructure.adapters.mapper;

import com.microservice.notification.domain.model.Notification;
import com.microservice.notification.infrastructure.entities.NotificationEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Notification domain model and NotificationEntity.
 */
@Component
public class NotificationMapper {

    private final NotificationTemplateMapper templateMapper;
    private final NotificationStatusMapper statusMapper;

    public NotificationMapper(NotificationTemplateMapper templateMapper,
                              NotificationStatusMapper statusMapper) {
        this.templateMapper = templateMapper;
        this.statusMapper = statusMapper;
    }

    /**
     * Converts a NotificationEntity to a Notification domain model.
     *
     * @param entity the entity to convert
     * @return the domain model
     */
    public Notification toDomain(NotificationEntity entity) {
        if (entity == null) {
            return null;
        }

        Notification notification = new Notification();
        notification.setId(entity.getId());
        notification.setExternalUserProfileId(entity.getExternalUserProfileId());
        notification.setChannel(entity.getChannel());
        notification.setTitle(entity.getTitle());
        notification.setMessage(entity.getMessage());
        notification.setRecipientEmail(entity.getRecipientEmail());
        notification.setRecipientPhone(entity.getRecipientPhone());
        notification.setRecipientDeviceToken(entity.getRecipientDeviceToken());
        notification.setPriority(entity.getPriority());
        notification.setReferenceType(entity.getReferenceType());
        notification.setExternalReferenceId(entity.getExternalReferenceId());
        notification.setActionUrl(entity.getActionUrl());
        notification.setMetadata(entity.getMetadata());
        notification.setSentAt(entity.getSentAt());
        notification.setDeliveredAt(entity.getDeliveredAt());
        notification.setReadAt(entity.getReadAt());
        notification.setFailedReason(entity.getFailedReason());
        notification.setRetryCount(entity.getRetryCount());
        notification.setDeleted(entity.isDeleted());
        notification.setCreatedAt(entity.getCreatedAt());
        notification.setUpdatedAt(entity.getUpdatedAt());
        notification.setDeletedAt(entity.getDeletedAt());

        // Map related entities
        if (entity.getTemplate() != null) {
            notification.setTemplate(templateMapper.toDomain(entity.getTemplate()));
        }
        if (entity.getStatus() != null) {
            notification.setStatus(statusMapper.toDomain(entity.getStatus()));
        }

        return notification;
    }

    /**
     * Converts a Notification domain model to a NotificationEntity.
     *
     * @param domain the domain model to convert
     * @return the entity
     */
    public NotificationEntity toEntity(Notification domain) {
        if (domain == null) {
            return null;
        }

        NotificationEntity entity = new NotificationEntity();
        entity.setId(domain.getId());
        entity.setExternalUserProfileId(domain.getExternalUserProfileId());
        entity.setChannel(domain.getChannel());
        entity.setTitle(domain.getTitle());
        entity.setMessage(domain.getMessage());
        entity.setRecipientEmail(domain.getRecipientEmail());
        entity.setRecipientPhone(domain.getRecipientPhone());
        entity.setRecipientDeviceToken(domain.getRecipientDeviceToken());
        entity.setPriority(domain.getPriority());
        entity.setReferenceType(domain.getReferenceType());
        entity.setExternalReferenceId(domain.getExternalReferenceId());
        entity.setActionUrl(domain.getActionUrl());
        entity.setMetadata(domain.getMetadata());
        entity.setSentAt(domain.getSentAt());
        entity.setDeliveredAt(domain.getDeliveredAt());
        entity.setReadAt(domain.getReadAt());
        entity.setFailedReason(domain.getFailedReason());
        entity.setRetryCount(domain.getRetryCount());
        entity.setDeleted(domain.isDeleted());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setDeletedAt(domain.getDeletedAt());

        // Map related domain objects
        if (domain.getTemplate() != null) {
            entity.setTemplate(templateMapper.toEntity(domain.getTemplate()));
        }
        if (domain.getStatus() != null) {
            entity.setStatus(statusMapper.toEntity(domain.getStatus()));
        }

        return entity;
    }
}
