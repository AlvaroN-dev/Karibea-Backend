package com.microservice.notification.domain.port.in.notification;

import com.microservice.notification.domain.model.Notification;

/**
 * Use case for creating a new notification.
 */
public interface CreateNotificationUseCase {

    /**
     * Creates a new notification.
     *
     * @param notification the notification to create
     * @return the created notification with generated ID
     */
    Notification create(Notification notification);
}
