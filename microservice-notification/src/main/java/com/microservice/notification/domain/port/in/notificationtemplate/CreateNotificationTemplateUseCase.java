package com.microservice.notification.domain.port.in.notificationtemplate;

import com.microservice.notification.domain.model.NotificationTemplate;

public interface CreateNotificationTemplateUseCase {
    NotificationTemplate create(NotificationTemplate template);
}

