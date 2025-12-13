package com.microservice.notification.domain.port.in.notificationtemplate;

import java.util.List;

import com.microservice.notification.domain.model.NotificationTemplate;

public interface ListNotificationTemplatesUseCase {
    List<NotificationTemplate> listActive();
}


