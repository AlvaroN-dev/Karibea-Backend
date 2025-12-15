package com.microservice.notification.domain.port.in.notification;

import java.util.List;

import com.microservice.notification.domain.model.Notification;

public interface ListNotificationsUseCase {
    List<Notification> listByExternalUser(String externalUserProfileId);
}

