package com.microservice.notification.domain.port.in.notificationstatus;

import java.util.List;

import com.microservice.notification.domain.model.NotificationStatus;

public interface ListNotificationStatusUseCase {
    List<NotificationStatus> listAll();
}
