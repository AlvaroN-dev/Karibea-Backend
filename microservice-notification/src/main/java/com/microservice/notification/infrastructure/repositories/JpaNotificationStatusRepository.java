package com.microservice.notification.infrastructure.repositories;

import com.microservice.notification.infrastructure.entities.NotificationStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaNotificationStatusRepository extends JpaRepository<NotificationStatusEntity, UUID> {
    Optional<NotificationStatusEntity> findByName(String name);
}
