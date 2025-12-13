package com.microservice.notification.infrastructure.repositories;

import com.microservice.notification.infrastructure.entities.UserNotificationPreferencesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUserNotificationPreferencesRepository
        extends JpaRepository<UserNotificationPreferencesEntity, Long> {
    Optional<UserNotificationPreferencesEntity> findByExternalUserId(String externalUserId);
}
