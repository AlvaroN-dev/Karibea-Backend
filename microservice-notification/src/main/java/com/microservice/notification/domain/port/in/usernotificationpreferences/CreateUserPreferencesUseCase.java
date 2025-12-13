package com.microservice.notification.domain.port.in.usernotificationpreferences;

import com.microservice.notification.domain.model.UserNotificationPreferences;

public interface CreateUserPreferencesUseCase {
    UserNotificationPreferences create(UserNotificationPreferences preferences);
}

