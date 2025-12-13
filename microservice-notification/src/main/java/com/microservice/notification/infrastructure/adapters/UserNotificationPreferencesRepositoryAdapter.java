package com.microservice.notification.infrastructure.adapters;

import com.microservice.notification.domain.model.UserNotificationPreferences;
import com.microservice.notification.domain.port.out.UserNotificationPreferencesRepositoryPort;
import com.microservice.notification.infrastructure.adapters.mapper.NotificationMapper;
import com.microservice.notification.infrastructure.entities.UserNotificationPreferencesEntity;
import com.microservice.notification.infrastructure.repositories.JpaUserNotificationPreferencesRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserNotificationPreferencesRepositoryAdapter implements UserNotificationPreferencesRepositoryPort {

    private final JpaUserNotificationPreferencesRepository jpaUserNotificationPreferencesRepository;
    private final NotificationMapper mapper;

    public UserNotificationPreferencesRepositoryAdapter(
            JpaUserNotificationPreferencesRepository jpaUserNotificationPreferencesRepository,
            NotificationMapper mapper) {
        this.jpaUserNotificationPreferencesRepository = jpaUserNotificationPreferencesRepository;
        this.mapper = mapper;
    }

    @Override
    public UserNotificationPreferences save(UserNotificationPreferences preferences) {
        UserNotificationPreferencesEntity entity = mapper.toEntity(preferences);
        return mapper.toDomain(jpaUserNotificationPreferencesRepository.save(entity));
    }

    @Override
    public Optional<UserNotificationPreferences> findByExternalUserId(String externalUserId) {
        return jpaUserNotificationPreferencesRepository.findByExternalUserId(externalUserId).map(mapper::toDomain);
    }

    @Override
    public void deleteByExternalUserId(String externalUserId) {
        Optional<UserNotificationPreferencesEntity> entity = jpaUserNotificationPreferencesRepository
                .findByExternalUserId(externalUserId);
        entity.ifPresent(jpaUserNotificationPreferencesRepository::delete);
    }
}
