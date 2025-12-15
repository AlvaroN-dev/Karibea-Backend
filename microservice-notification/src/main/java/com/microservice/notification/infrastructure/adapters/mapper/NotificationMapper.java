package com.microservice.notification.infrastructure.adapters.mapper;

import com.microservice.notification.domain.model.DeviceToken;
import com.microservice.notification.domain.model.Notification;
import com.microservice.notification.domain.model.NotificationStatus;
import com.microservice.notification.domain.model.NotificationTemplate;
import com.microservice.notification.domain.model.UserNotificationPreferences;
import com.microservice.notification.infrastructure.entities.DeviceTokenEntity;
import com.microservice.notification.infrastructure.entities.NotificationEntity;
import com.microservice.notification.infrastructure.entities.NotificationStatusEntity;
import com.microservice.notification.infrastructure.entities.NotificationTemplateEntity;
import com.microservice.notification.infrastructure.entities.UserNotificationPreferencesEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public Notification toDomain(NotificationEntity entity) {
        if (entity == null)
            return null;
        Notification domain = new Notification();
        domain.setId(entity.getId());
        domain.setExternalUserProfileId(entity.getExternalUserProfileId());
        domain.setTemplate(toDomain(entity.getTemplate()));
        domain.setStatus(toDomain(entity.getStatus()));
        domain.setChannel(entity.getChannel());
        domain.setTitle(entity.getTitle());
        domain.setMessage(entity.getMessage());
        domain.setRecipientEmail(entity.getRecipientEmail());
        domain.setRecipientPhone(entity.getRecipientPhone());
        domain.setRecipientDeviceToken(entity.getRecipientDeviceToken());
        domain.setPriority(entity.getPriority());
        domain.setReferenceType(entity.getReferenceType());
        domain.setExternalReferenceId(entity.getExternalReferenceId());
        domain.setActionUrl(entity.getActionUrl());
        domain.setMetadata(entity.getMetadata()); // Assuming JSON string compatibility or need conversion
        domain.setSentAt(entity.getSentAt());
        domain.setDeliveredAt(entity.getDeliveredAt());
        domain.setReadAt(entity.getReadAt());
        domain.setFailedReason(entity.getFailedReason());
        domain.setRetryCount(entity.getRetryCount());
        domain.setDeleted(entity.isDeleted());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setUpdatedAt(entity.getUpdatedAt());
        domain.setDeletedAt(entity.getDeletedAt());
        return domain;
    }

    public NotificationEntity toEntity(Notification domain) {
        if (domain == null)
            return null;
        NotificationEntity entity = new NotificationEntity();
        entity.setId(domain.getId());
        entity.setExternalUserProfileId(domain.getExternalUserProfileId());
        entity.setTemplate(toEntity(domain.getTemplate()));
        entity.setStatus(toEntity(domain.getStatus()));
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
        return entity;
    }

    public NotificationTemplate toDomain(NotificationTemplateEntity entity) {
        if (entity == null)
            return null;
        NotificationTemplate domain = new NotificationTemplate();
        domain.setId(entity.getId());
        domain.setCode(entity.getCode());
        domain.setName(entity.getName());
        domain.setDescription(entity.getDescription());
        domain.setChannel(entity.getChannel());
        domain.setSubjectTemplate(entity.getSubjectTemplate());
        domain.setBodyTemplate(entity.getBodyTemplate());
        domain.setHtmlTemplate(entity.getHtmlTemplate());
        domain.setVariables(entity.getVariables());
        domain.setActive(entity.isActive());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setUpdatedAt(entity.getUpdatedAt());
        return domain;
    }

    public NotificationTemplateEntity toEntity(NotificationTemplate domain) {
        if (domain == null)
            return null;
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

    public NotificationStatus toDomain(NotificationStatusEntity entity) {
        if (entity == null)
            return null;
        NotificationStatus domain = new NotificationStatus();
        domain.setId(entity.getId());
        domain.setName(entity.getName());
        domain.setDescription(entity.getDescription());
        domain.setCreatedAt(entity.getCreatedAt());
        return domain;
    }

    public NotificationStatusEntity toEntity(NotificationStatus domain) {
        if (domain == null)
            return null;
        NotificationStatusEntity entity = new NotificationStatusEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }

    public UserNotificationPreferences toDomain(UserNotificationPreferencesEntity entity) {
        if (entity == null)
            return null;
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

    public UserNotificationPreferencesEntity toEntity(UserNotificationPreferences domain) {
        if (domain == null)
            return null;
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

    public DeviceToken toDomain(DeviceTokenEntity entity) {
        if (entity == null)
            return null;
        DeviceToken domain = new DeviceToken();
        domain.setId(entity.getId());
        domain.setExternalUserId(entity.getExternalUserId());
        domain.setDeviceToken(entity.getDeviceToken());
        domain.setPlatform(entity.getPlatform());
        domain.setActive(entity.isActive());
        domain.setLastUsedAt(entity.getLastUsedAt());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setUpdatedAt(entity.getUpdatedAt());
        return domain;
    }

    public DeviceTokenEntity toEntity(DeviceToken domain) {
        if (domain == null)
            return null;
        DeviceTokenEntity entity = new DeviceTokenEntity();
        entity.setId(domain.getId());
        entity.setExternalUserId(domain.getExternalUserId());
        entity.setDeviceToken(domain.getDeviceToken());
        entity.setPlatform(domain.getPlatform());
        entity.setActive(domain.isActive());
        entity.setLastUsedAt(domain.getLastUsedAt());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }
}
