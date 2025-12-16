package com.microservice.notification.infrastructure.adapters.mapper;

import com.microservice.notification.domain.model.UserNotificationPreferences;
import com.microservice.notification.infrastructure.entities.UserNotificationPreferencesEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between UserNotificationPreferences domain model and entity.
 */
@Component
public class UserNotificationPreferencesMapper {

    /**
     * Converts an entity to domain model.
     *
     * @param entity the entity to convert
     * @return the domain model
     */
    public UserNotificationPreferences toDomain(UserNotificationPreferencesEntity entity) {
        if (entity == null) {
            return null;
        }

        UserNotificationPreferences domain = new UserNotificationPreferences();
        domain.setId(entity.getId());
        domain.setExternalUserId(entity.getExternalUserId());
        domain.setEmailEnabled(entity.isEmailEnabled());
        domain.setPushEnabled(entity.isPushEnabled());
        domain.setInAppEnabled(entity.isInAppEnabled());
        domain.setPreferences(entity.getPreferences());
        domain.setQuietHoursStart(entity.getQuietHoursStart());
        domain.setQuietHoursEnd(entity.getQuietHoursEnd());
        domain.setTimezone(entity.getTimezone());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setUpdatedAt(entity.getUpdatedAt());

        return domain;
    }

    /**
     * Converts a domain model to entity.
     *
     * @param domain the domain model to convert
     * @return the entity
     */
    public UserNotificationPreferencesEntity toEntity(UserNotificationPreferences domain) {
        if (domain == null) {
            return null;
        }

        UserNotificationPreferencesEntity entity = new UserNotificationPreferencesEntity();
        entity.setId(domain.getId());
        entity.setExternalUserId(domain.getExternalUserId());
        entity.setEmailEnabled(domain.isEmailEnabled());
        entity.setPushEnabled(domain.isPushEnabled());
        entity.setInAppEnabled(domain.isInAppEnabled());
        entity.setPreferences(domain.getPreferences());
        entity.setQuietHoursStart(domain.getQuietHoursStart());
        entity.setQuietHoursEnd(domain.getQuietHoursEnd());
        entity.setTimezone(domain.getTimezone());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        return entity;
    }
}
