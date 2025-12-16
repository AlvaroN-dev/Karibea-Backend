package com.microservice.notification.domain.port.in.notification;

import java.util.UUID;

import com.microservice.notification.domain.model.Notification;

public interface GetNotificationUseCase {
    Notification getById(UUID id);
}
