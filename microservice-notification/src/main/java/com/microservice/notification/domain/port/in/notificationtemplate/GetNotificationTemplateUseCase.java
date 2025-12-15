package com.microservice.notification.domain.port.in.notificationtemplate;

import java.util.UUID;

import com.microservice.notification.domain.model.NotificationTemplate;

public interface GetNotificationTemplateUseCase {
    NotificationTemplate getById(UUID id);
}
