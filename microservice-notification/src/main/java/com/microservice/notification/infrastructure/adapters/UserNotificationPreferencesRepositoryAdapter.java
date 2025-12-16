package com.microservice.notification.infrastructure.adapters;

import com.microservice.notification.domain.model.UserNotificationPreferences;
import com.microservice.notification.domain.port.out.UserNotificationPreferencesRepositoryPort;
import com.microservice.notification.infrastructure.adapters.mapper.UserNotificationPreferencesMapper;
import com.microservice.notification.infrastructure.entities.UserNotificationPreferencesEntity;
import com.microservice.notification.infrastructure.repositories.JpaUserNotificationPreferencesRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserNotificationPreferencesRepositoryAdapter implements UserNotificationPreferencesRepositoryPort {

    private final JpaUserNotificationPreferencesRepository repository;
    private final UserNotificationPreferencesMapper mapper;

    public UserNotificationPreferencesRepositoryAdapter(
            JpaUserNotificationPreferencesRepository repository,
            UserNotificationPreferencesMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public UserNotificationPreferences save(UserNotificationPreferences preferences) {
        UserNotificationPreferencesEntity entity = mapper.toEntity(preferences);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<UserNotificationPreferences> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<UserNotificationPreferences> findByExternalUserId(String externalUserId) {
        return repository.findByExternalUserId(externalUserId).map(mapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
