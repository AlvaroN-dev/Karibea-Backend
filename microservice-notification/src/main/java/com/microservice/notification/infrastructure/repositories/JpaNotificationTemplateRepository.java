package com.microservice.notification.infrastructure.repositories;

import com.microservice.notification.infrastructure.entities.NotificationTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaNotificationTemplateRepository extends JpaRepository<NotificationTemplateEntity, UUID> {
    Optional<NotificationTemplateEntity> findByCode(String code);

    List<NotificationTemplateEntity> findByIsActiveTrue();
}
