package com.microservice.notification.infrastructure.repositories;

import com.microservice.notification.infrastructure.entities.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaNotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByExternalUserProfileId(String externalUserProfileId);
}
