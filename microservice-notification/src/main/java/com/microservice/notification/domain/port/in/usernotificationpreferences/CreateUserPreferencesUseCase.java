package com.microservice.notification.domain.port.in.usernotificationpreferences;

import com.microservice.notification.domain.model.UserNotificationPreferences;

/**
 * Use case for creating user notification preferences.
 */
public interface CreateUserPreferencesUseCase {

    /**
     * Creates notification preferences for a user.
     *
     * @param preferences the preferences to create
     * @return the created preferences
     */
    UserNotificationPreferences create(UserNotificationPreferences preferences);
}
