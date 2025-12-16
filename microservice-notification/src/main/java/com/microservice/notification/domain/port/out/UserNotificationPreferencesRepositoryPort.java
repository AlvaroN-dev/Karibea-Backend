package com.microservice.notification.domain.port.out;

import com.microservice.notification.domain.model.UserNotificationPreferences;

import java.util.Optional;
import java.util.UUID;

/**
 * Output port for user notification preferences persistence operations.
 */
public interface UserNotificationPreferencesRepositoryPort {

    /**
     * Saves user notification preferences.
     *
     * @param preferences the preferences to save
     * @return the saved preferences
     */
    UserNotificationPreferences save(UserNotificationPreferences preferences);

    /**
     * Finds preferences by ID.
     *
     * @param id the preferences ID
     * @return optional containing the preferences if found
     */
    Optional<UserNotificationPreferences> findById(UUID id);

    /**
     * Finds preferences by external user ID.
     *
     * @param externalUserId the external user ID
     * @return optional containing the preferences if found
     */
    Optional<UserNotificationPreferences> findByExternalUserId(String externalUserId);

    /**
     * Deletes preferences by ID.
     *
     * @param id the preferences ID
     */
    void deleteById(UUID id);
}
