package com.microservice.notification.domain.port.in.notificationtemplate;

import com.microservice.notification.domain.model.NotificationTemplate;

import java.util.List;

/**
 * Use case for listing notification templates.
 */
public interface ListNotificationTemplatesUseCase {

    /**
     * Lists all active notification templates.
     *
     * @return list of active notification templates
     */
    List<NotificationTemplate> listActive();
}
