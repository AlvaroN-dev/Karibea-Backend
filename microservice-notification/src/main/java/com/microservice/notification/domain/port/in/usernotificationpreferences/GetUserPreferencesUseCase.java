package com.microservice.notification.domain.port.in.usernotificationpreferences;

import com.microservice.notification.domain.model.UserNotificationPreferences;

/**
 * Use case for retrieving user notification preferences.
 */
public interface GetUserPreferencesUseCase {

    /**
     * Gets notification preferences for a user by their external ID.
     *
     * @param externalUserId the external user ID
     * @return the user's notification preferences, or null if not found
     */
    UserNotificationPreferences getByExternalUserId(String externalUserId);
}
