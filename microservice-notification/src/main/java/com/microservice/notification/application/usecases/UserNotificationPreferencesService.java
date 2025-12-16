package com.microservice.notification.application.usecases;

import com.microservice.notification.domain.model.UserNotificationPreferences;
import com.microservice.notification.domain.port.in.usernotificationpreferences.CreateUserPreferencesUseCase;
import com.microservice.notification.domain.port.in.usernotificationpreferences.GetUserPreferencesUseCase;
import com.microservice.notification.domain.port.in.usernotificationpreferences.UpdateUserPreferencesUseCase;
import com.microservice.notification.domain.port.out.UserNotificationPreferencesRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class UserNotificationPreferencesService 
        implements CreateUserPreferencesUseCase, GetUserPreferencesUseCase, UpdateUserPreferencesUseCase {

    private final UserNotificationPreferencesRepositoryPort repository;

    public UserNotificationPreferencesService(UserNotificationPreferencesRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public UserNotificationPreferences create(UserNotificationPreferences preferences) {
        if (preferences.getCreatedAt() == null) {
            preferences.setCreatedAt(Instant.now());
        }
        return repository.save(preferences);
    }

    @Override
    @Transactional(readOnly = true)
    public UserNotificationPreferences getByExternalUserId(String externalUserId) {
        return repository.findByExternalUserId(externalUserId).orElse(null);
    }

    @Override
    @Transactional
    public UserNotificationPreferences update(String externalUserId, UserNotificationPreferences preferences) {
        UserNotificationPreferences existing = repository.findByExternalUserId(externalUserId)
                .orElseThrow(() -> new RuntimeException("Preferences not found for user: " + externalUserId));

        preferences.setId(existing.getId());
        preferences.setExternalUserId(externalUserId);
        preferences.setCreatedAt(existing.getCreatedAt());
        preferences.setUpdatedAt(Instant.now());

        return repository.save(preferences);
    }
}
