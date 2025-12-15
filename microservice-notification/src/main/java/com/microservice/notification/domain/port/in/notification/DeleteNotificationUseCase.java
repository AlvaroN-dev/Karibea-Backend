package com.microservice.notification.domain.port.in.notification;

import java.util.UUID;

public interface DeleteNotificationUseCase {
    void delete(UUID id);
}
