package com.microservice.notification.application.usecases;

import com.microservice.notification.domain.model.UserNotificationPreferences;
import com.microservice.notification.domain.port.in.usernotificationpreferences.CreateUserPreferencesUseCase;
import com.microservice.notification.domain.port.in.usernotificationpreferences.DeleteUserPreferencesUseCase;
import com.microservice.notification.domain.port.in.usernotificationpreferences.GetUserPreferencesUseCase;
import com.microservice.notification.domain.port.in.usernotificationpreferences.UpdateUserPreferencesUseCase;
import com.microservice.notification.domain.port.out.UserNotificationPreferencesRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class UserPreferencesService implements CreateUserPreferencesUseCase, UpdateUserPreferencesUseCase,
        DeleteUserPreferencesUseCase, GetUserPreferencesUseCase {

    private final UserNotificationPreferencesRepositoryPort repository;

    public UserPreferencesService(UserNotificationPreferencesRepositoryPort repository) {
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
    @Transactional
    public UserNotificationPreferences update(String externalUserId, UserNotificationPreferences preferences) {
        UserNotificationPreferences existing = repository.findByExternalUserId(externalUserId).orElse(null);
        if (existing != null) {
            preferences.setId(existing.getId());
        }
        if (preferences.getUpdatedAt() == null) {
            preferences.setUpdatedAt(Instant.now());
        }
        return repository.save(preferences);
    }

    @Override
    @Transactional
    public void delete(String externalUserId) {
        repository.deleteByExternalUserId(externalUserId);
    }

    @Override
    @Transactional(readOnly = true)
    public UserNotificationPreferences getByExternalUserId(String externalUserId) {
        return repository.findByExternalUserId(externalUserId).orElse(null);
    }
}
