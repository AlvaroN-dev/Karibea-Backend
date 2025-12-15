package com.microservice.notification.application.usecases;

import com.microservice.notification.domain.model.NotificationTemplate;
import com.microservice.notification.domain.port.in.notificationtemplate.CreateNotificationTemplateUseCase;
import com.microservice.notification.domain.port.in.notificationtemplate.DeleteNotificationTemplateUseCase;
import com.microservice.notification.domain.port.in.notificationtemplate.GetNotificationTemplateUseCase;
import com.microservice.notification.domain.port.in.notificationtemplate.ListNotificationTemplatesUseCase;
import com.microservice.notification.domain.port.in.notificationtemplate.UpdateNotificationTemplateUseCase;
import com.microservice.notification.domain.port.out.NotificationTemplateRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationTemplateService
        implements CreateNotificationTemplateUseCase, UpdateNotificationTemplateUseCase,
        DeleteNotificationTemplateUseCase, GetNotificationTemplateUseCase, ListNotificationTemplatesUseCase {

    private final NotificationTemplateRepositoryPort notificationTemplateRepositoryPort;

    public NotificationTemplateService(NotificationTemplateRepositoryPort notificationTemplateRepositoryPort) {
        this.notificationTemplateRepositoryPort = notificationTemplateRepositoryPort;
    }

    @Override
    @Transactional
    public NotificationTemplate create(NotificationTemplate template) {
        if (template.getCreatedAt() == null) {
            template.setCreatedAt(Instant.now());
        }
        return notificationTemplateRepositoryPort.save(template);
    }

    @Override
    @Transactional
    public NotificationTemplate update(UUID id, NotificationTemplate template) {
        if (template.getUpdatedAt() == null) {
            template.setUpdatedAt(Instant.now());
        }
        template.setId(id);
        return notificationTemplateRepositoryPort.save(template);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        notificationTemplateRepositoryPort.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationTemplate getById(UUID id) {
        return notificationTemplateRepositoryPort.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationTemplate> listActive() {
        return notificationTemplateRepositoryPort.findActive();
    }
}
