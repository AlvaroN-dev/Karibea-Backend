package com.microservice.notification.domain.port.in.notification;

import com.microservice.notification.domain.model.Notification;

public interface CreateNotificationUseCase {
    Notification create(Notification notification);
}

