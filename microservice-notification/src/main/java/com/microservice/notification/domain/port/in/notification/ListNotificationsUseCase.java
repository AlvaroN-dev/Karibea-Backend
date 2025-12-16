package com.microservice.notification.domain.port.in.notification;

import com.microservice.notification.domain.model.Notification;

import java.util.List;

/**
 * Use case for listing notifications.
 */
public interface ListNotificationsUseCase {

    /**
     * Lists all notifications for a specific external user profile.
     *
     * @param externalUserProfileId the external user profile ID
     * @return list of notifications for the user
     */
    List<Notification> listByExternalUser(String externalUserProfileId);
}
