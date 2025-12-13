package com.microservice.notification.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.microservice.notification.domain.model.NotificationStatus;

public interface NotificationStatusRepositoryPort {

    Optional<NotificationStatus> findById(Long id);

    List<NotificationStatus> findAll();
}

