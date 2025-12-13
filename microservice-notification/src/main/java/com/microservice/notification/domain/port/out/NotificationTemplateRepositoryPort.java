package com.microservice.notification.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.microservice.notification.domain.model.NotificationTemplate;

public interface NotificationTemplateRepositoryPort {

    NotificationTemplate save(NotificationTemplate template);

    Optional<NotificationTemplate> findById(Long id);

    List<NotificationTemplate> findActive();

    void deleteById(Long id);
}

