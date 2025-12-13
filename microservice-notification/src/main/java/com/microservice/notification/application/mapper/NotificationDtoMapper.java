package com.microservice.notification.application.mapper;

import com.microservice.notification.application.dto.*;
import com.microservice.notification.domain.model.DeviceToken;
import com.microservice.notification.domain.model.Notification;
import com.microservice.notification.domain.model.UserNotificationPreferences;
import org.springframework.stereotype.Component;

@Component
public class NotificationDtoMapper {

    public Notification toDomain(NotificationRequest request) {
        if (request == null)
            return null;
        Notification notification = new Notification();
        notification.setExternalUserProfileId(request.getExternalUserProfileId());
        // template and status handling might require fetching entities, but simple
        // mapping here
        // Ideally we pass IDs and let service handle lookup, or map if we have full
        // objects
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
        if (domain.getStatus() != null) {
            response.setStatus(domain.getStatus().getName());
        }
        response.setSentAt(domain.getSentAt());
        response.setDeliveredAt(domain.getDeliveredAt());
        response.setReadAt(domain.getReadAt());
        response.setCreatedAt(domain.getCreatedAt());
        return response;
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
        return response;
    }
}
