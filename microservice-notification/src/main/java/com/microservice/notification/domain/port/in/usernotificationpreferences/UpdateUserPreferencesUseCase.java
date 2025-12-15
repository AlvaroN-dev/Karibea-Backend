package com.microservice.notification.domain.port.in.usernotificationpreferences;

import com.microservice.notification.domain.model.UserNotificationPreferences;

public interface UpdateUserPreferencesUseCase {
    UserNotificationPreferences update(String externalUserId, UserNotificationPreferences preferences);
}

