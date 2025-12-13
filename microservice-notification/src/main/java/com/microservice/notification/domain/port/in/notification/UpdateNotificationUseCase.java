package com.microservice.notification.domain.port.in.notification;

import com.microservice.notification.domain.model.Notification;

public interface UpdateNotificationUseCase {
    Notification update(Long id, Notification notification);
}
