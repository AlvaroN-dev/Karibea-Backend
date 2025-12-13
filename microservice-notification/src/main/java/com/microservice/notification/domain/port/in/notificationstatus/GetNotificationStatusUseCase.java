package com.microservice.notification.domain.port.in.notificationstatus;

import com.microservice.notification.domain.model.NotificationStatus;

public interface GetNotificationStatusUseCase {
    NotificationStatus getById(Long id);
}
