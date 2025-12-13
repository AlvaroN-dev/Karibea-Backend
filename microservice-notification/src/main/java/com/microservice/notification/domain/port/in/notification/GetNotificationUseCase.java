package com.microservice.notification.domain.port.in.notification;

import com.microservice.notification.domain.model.Notification;

public interface GetNotificationUseCase {
    Notification getById(Long id);
}
