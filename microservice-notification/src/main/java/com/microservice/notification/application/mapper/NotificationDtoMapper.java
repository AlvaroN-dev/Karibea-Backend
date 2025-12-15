package com.microservice.notification.application.mapper;

import com.microservice.notification.application.dto.*;
import com.microservice.notification.application.dto.NotificationResponse.NotificationStatusInfo;
import com.microservice.notification.application.dto.NotificationResponse.NotificationTemplateInfo;
import com.microservice.notification.domain.model.DeviceToken;
import com.microservice.notification.domain.model.Notification;
import com.microservice.notification.domain.model.NotificationStatus;
import com.microservice.notification.domain.model.NotificationTemplate;
import com.microservice.notification.domain.model.UserNotificationPreferences;
import org.springframework.stereotype.Component;

@Component
public class NotificationDtoMapper {

    public Notification toDomain(NotificationRequest request) {
        if (request == null)
            return null;
        Notification notification = new Notification();
        notification.setExternalUserProfileId(request.getExternalUserProfileId());
        notification.setChannel(request.getChannel());
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setRecipientEmail(request.getRecipientEmail());
        notification.setRecipientPhone(request.getRecipientPhone());
        notification.setRecipientDeviceToken(request.getRecipientDeviceToken());
        notification.setPriority(request.getPriority());
        notification.setReferenceType(request.getReferenceType());
        notification.setExternalReferenceId(request.getExternalReferenceId());
        notification.setActionUrl(request.getActionUrl());
        notification.setMetadata(request.getMetadata());
        return notification;
    }

    public NotificationResponse toResponse(Notification domain) {
        if (domain == null)
            return null;
        NotificationResponse response = new NotificationResponse();
        response.setId(domain.getId());
        response.setExternalUserProfileId(domain.getExternalUserProfileId());
        response.setChannel(domain.getChannel());
        response.setTitle(domain.getTitle());
        response.setMessage(domain.getMessage());
        response.setRecipientEmail(domain.getRecipientEmail());
        response.setRecipientPhone(domain.getRecipientPhone());
        response.setPriority(domain.getPriority());
        response.setReferenceType(domain.getReferenceType());
        response.setExternalReferenceId(domain.getExternalReferenceId());
        response.setActionUrl(domain.getActionUrl());
        response.setMetadata(domain.getMetadata());
        
        // Map embedded status info
        if (domain.getStatus() != null) {
            response.setStatus(toStatusInfo(domain.getStatus()));
        }
        
        // Map embedded template info
        if (domain.getTemplate() != null) {
            response.setTemplate(toTemplateInfo(domain.getTemplate()));
        }
        
        response.setSentAt(domain.getSentAt());
        response.setDeliveredAt(domain.getDeliveredAt());
        response.setReadAt(domain.getReadAt());
        response.setCreatedAt(domain.getCreatedAt());
        return response;
    }

    private NotificationStatusInfo toStatusInfo(NotificationStatus status) {
        NotificationStatusInfo info = new NotificationStatusInfo();
        info.setId(status.getId());
        info.setName(status.getName());
        info.setDescription(status.getDescription());
        return info;
    }

    private NotificationTemplateInfo toTemplateInfo(NotificationTemplate template) {
        NotificationTemplateInfo info = new NotificationTemplateInfo();
        info.setId(template.getId());
        info.setCode(template.getCode());
        info.setName(template.getName());
        info.setChannel(template.getChannel());
        return info;
    }

    public DeviceToken toDomain(DeviceTokenRequest request) {
        if (request == null)
            return null;
        DeviceToken domain = new DeviceToken();
        domain.setExternalUserId(request.getExternalUserId());
        domain.setDeviceToken(request.getDeviceToken());
        domain.setPlatform(request.getPlatform());
        return domain;
    }

    public DeviceTokenResponse toResponse(DeviceToken domain) {
        if (domain == null)
            return null;
        DeviceTokenResponse response = new DeviceTokenResponse();
        response.setId(domain.getId());
        response.setExternalUserId(domain.getExternalUserId());
        response.setDeviceToken(domain.getDeviceToken());
        response.setPlatform(domain.getPlatform());
        response.setActive(domain.isActive());
        response.setLastUsedAt(domain.getLastUsedAt());
        response.setCreatedAt(domain.getCreatedAt());
        response.setUpdatedAt(domain.getUpdatedAt());
        return response;
    }

    public UserNotificationPreferences toDomain(UserPreferencesRequest request) {
        if (request == null)
            return null;
        UserNotificationPreferences domain = new UserNotificationPreferences();
        domain.setExternalUserId(request.getExternalUserId());
        domain.setEmailEnabled(request.isEmailEnabled());
        domain.setPushEnabled(request.isPushEnabled());
        domain.setInAppEnabled(request.isInAppEnabled());
        domain.setPreferences(request.getPreferences());
        domain.setQuietHoursStart(request.getQuietHoursStart());
        domain.setQuietHoursEnd(request.getQuietHoursEnd());
        domain.setTimezone(request.getTimezone());
        return domain;
    }

    public UserPreferencesResponse toResponse(UserNotificationPreferences domain) {
        if (domain == null)
            return null;
        UserPreferencesResponse response = new UserPreferencesResponse();
        response.setId(domain.getId());
        response.setExternalUserId(domain.getExternalUserId());
        response.setEmailEnabled(domain.isEmailEnabled());
        response.setPushEnabled(domain.isPushEnabled());
        response.setInAppEnabled(domain.isInAppEnabled());
        response.setPreferences(domain.getPreferences());
        response.setQuietHoursStart(domain.getQuietHoursStart());
        response.setQuietHoursEnd(domain.getQuietHoursEnd());
        response.setTimezone(domain.getTimezone());
        response.setCreatedAt(domain.getCreatedAt());
        response.setUpdatedAt(domain.getUpdatedAt());
        return response;
    }
}
