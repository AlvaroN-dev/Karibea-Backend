package com.microservice.notification.domain.port.in.usernotificationpreferences;

import com.microservice.notification.domain.model.UserNotificationPreferences;

/**
 * Use case for updating user notification preferences.
 */
public interface UpdateUserPreferencesUseCase {

    /**
     * Updates notification preferences for a user.
     *
     * @param externalUserId the external user ID
     * @param preferences the updated preferences
     * @return the updated preferences
     */
    UserNotificationPreferences update(String externalUserId, UserNotificationPreferences preferences);
}
