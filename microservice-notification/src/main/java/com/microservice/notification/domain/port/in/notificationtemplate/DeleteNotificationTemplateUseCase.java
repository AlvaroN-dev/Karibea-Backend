package com.microservice.notification.domain.port.in.notificationtemplate;

import java.util.UUID;

public interface DeleteNotificationTemplateUseCase {
    void delete(UUID id);
}
