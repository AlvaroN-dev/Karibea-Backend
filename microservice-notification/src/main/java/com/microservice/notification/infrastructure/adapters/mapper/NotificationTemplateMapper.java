package com.microservice.notification.infrastructure.adapters.mapper;

import com.microservice.notification.domain.model.NotificationTemplate;
import com.microservice.notification.infrastructure.entities.NotificationTemplateEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between NotificationTemplate domain model and NotificationTemplateEntity.
 */
@Component
public class NotificationTemplateMapper {

    /**
     * Converts a NotificationTemplateEntity to a NotificationTemplate domain model.
     *
     * @param entity the entity to convert
     * @return the domain model
     */
    public NotificationTemplate toDomain(NotificationTemplateEntity entity) {
        if (entity == null) {
            return null;
        }

        NotificationTemplate template = new NotificationTemplate();
        template.setId(entity.getId());
        template.setCode(entity.getCode());
        template.setName(entity.getName());
        template.setDescription(entity.getDescription());
        template.setChannel(entity.getChannel());
        template.setSubjectTemplate(entity.getSubjectTemplate());
        template.setBodyTemplate(entity.getBodyTemplate());
        template.setHtmlTemplate(entity.getHtmlTemplate());
        template.setVariables(entity.getVariables());
        template.setActive(entity.isActive());
        template.setCreatedAt(entity.getCreatedAt());
        template.setUpdatedAt(entity.getUpdatedAt());

        return template;
    }

    /**
     * Converts a NotificationTemplate domain model to a NotificationTemplateEntity.
     *
     * @param domain the domain model to convert
     * @return the entity
     */
    public NotificationTemplateEntity toEntity(NotificationTemplate domain) {
        if (domain == null) {
            return null;
        }

        NotificationTemplateEntity entity = new NotificationTemplateEntity();
        entity.setId(domain.getId());
        entity.setCode(domain.getCode());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setChannel(domain.getChannel());
        entity.setSubjectTemplate(domain.getSubjectTemplate());
        entity.setBodyTemplate(domain.getBodyTemplate());
        entity.setHtmlTemplate(domain.getHtmlTemplate());
        entity.setVariables(domain.getVariables());
        entity.setActive(domain.isActive());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        return entity;
    }
}
