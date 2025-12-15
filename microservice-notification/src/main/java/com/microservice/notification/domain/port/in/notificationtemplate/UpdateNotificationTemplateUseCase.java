package com.microservice.notification.domain.port.in.notificationtemplate;

import java.util.UUID;

import com.microservice.notification.domain.model.NotificationTemplate;

public interface UpdateNotificationTemplateUseCase {
    NotificationTemplate update(UUID id, NotificationTemplate template);
}
