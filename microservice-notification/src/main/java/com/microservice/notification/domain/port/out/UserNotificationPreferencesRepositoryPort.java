package com.microservice.notification.domain.port.out;

import java.util.Optional;

import com.microservice.notification.domain.model.UserNotificationPreferences;

public interface UserNotificationPreferencesRepositoryPort {

    UserNotificationPreferences save(UserNotificationPreferences preferences);

    Optional<UserNotificationPreferences> findByExternalUserId(String externalUserId);

    void deleteByExternalUserId(String externalUserId);
}
