package com.microservice.notification.infrastructure.repositories;

import com.microservice.notification.infrastructure.entities.NotificationStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaNotificationStatusRepository extends JpaRepository<NotificationStatusEntity, Long> {
    Optional<NotificationStatusEntity> findByName(String name);
}
