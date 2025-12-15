package com.microservice.notification.domain.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.microservice.notification.domain.model.NotificationStatus;

public interface NotificationStatusRepositoryPort {

    Optional<NotificationStatus> findById(UUID id);

    List<NotificationStatus> findAll();
}
