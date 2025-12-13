package com.microservice.notification.domain.port.in.notificationtemplate;

import com.microservice.notification.domain.model.NotificationTemplate;

public interface UpdateNotificationTemplateUseCase {
    NotificationTemplate update(Long id, NotificationTemplate template);
}


