package com.microservice.notification.domain.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.microservice.notification.domain.model.Notification;

public interface NotificationRepositoryPort {

    Notification save(Notification notification);

    Optional<Notification> findById(UUID id);

    List<Notification> findByExternalUserProfileId(String externalUserProfileId);

    void deleteById(UUID id);
}
