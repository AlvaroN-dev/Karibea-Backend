package com.microservice.notification.domain.port.in.notificationtemplate;

import com.microservice.notification.domain.model.NotificationTemplate;

/**
 * Use case for creating a new notification template.
 */
public interface CreateNotificationTemplateUseCase {

    /**
     * Creates a new notification template.
     *
     * @param template the notification template to create
     * @return the created notification template
     */
    NotificationTemplate create(NotificationTemplate template);
}
