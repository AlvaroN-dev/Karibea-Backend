package com.microservice.notification.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.microservice.notification.domain.model.Notification;

public interface NotificationRepositoryPort {

    Notification save(Notification notification);

    Optional<Notification> findById(Long id);

    List<Notification> findByExternalUserProfileId(String externalUserProfileId);

    void deleteById(Long id);
}

