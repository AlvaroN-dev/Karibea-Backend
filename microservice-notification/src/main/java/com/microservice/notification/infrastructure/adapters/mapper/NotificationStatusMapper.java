package com.microservice.notification.infrastructure.adapters.mapper;

import com.microservice.notification.domain.model.NotificationStatus;
import com.microservice.notification.infrastructure.entities.NotificationStatusEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between NotificationStatus domain model and NotificationStatusEntity.
 */
@Component
public class NotificationStatusMapper {

    /**
     * Converts a NotificationStatusEntity to a NotificationStatus domain model.
     *
     * @param entity the entity to convert
     * @return the domain model
     */
    public NotificationStatus toDomain(NotificationStatusEntity entity) {
        if (entity == null) {
            return null;
        }

        NotificationStatus status = new NotificationStatus();
        status.setId(entity.getId());
        status.setName(entity.getName());
        status.setDescription(entity.getDescription());
        status.setCreatedAt(entity.getCreatedAt());

        return status;
    }

    /**
     * Converts a NotificationStatus domain model to a NotificationStatusEntity.
     *
     * @param domain the domain model to convert
     * @return the entity
     */
    public NotificationStatusEntity toEntity(NotificationStatus domain) {
        if (domain == null) {
            return null;
        }

        NotificationStatusEntity entity = new NotificationStatusEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setCreatedAt(domain.getCreatedAt());

        return entity;
    }
}
