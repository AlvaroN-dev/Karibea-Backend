package com.microservice.notification.application.usecases;

import com.microservice.notification.domain.model.Notification;
import com.microservice.notification.domain.port.in.notification.CreateNotificationUseCase;
import com.microservice.notification.domain.port.in.notification.DeleteNotificationUseCase;
import com.microservice.notification.domain.port.in.notification.GetNotificationUseCase;
import com.microservice.notification.domain.port.in.notification.ListNotificationsUseCase;
import com.microservice.notification.domain.port.in.notification.UpdateNotificationUseCase;
import com.microservice.notification.domain.port.out.NotificationRepositoryPort;
import com.microservice.notification.domain.port.out.EventPublisherPort;
import com.microservice.notification.domain.events.NotificationCreatedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class NotificationService implements CreateNotificationUseCase, UpdateNotificationUseCase,
        DeleteNotificationUseCase, GetNotificationUseCase, ListNotificationsUseCase {

    private final NotificationRepositoryPort notificationRepositoryPort;
    private final EventPublisherPort eventPublisherPort;

    public NotificationService(NotificationRepositoryPort notificationRepositoryPort,
            EventPublisherPort eventPublisherPort) {
        this.notificationRepositoryPort = notificationRepositoryPort;
        this.eventPublisherPort = eventPublisherPort;
    }

    @Override
    @Transactional
    public Notification create(Notification notification) {
        if (notification.getCreatedAt() == null) {
            notification.setCreatedAt(Instant.now());
        }
        Notification saved = notificationRepositoryPort.save(notification);

        // Publish event
        NotificationCreatedEvent event = new NotificationCreatedEvent(
                saved.getId(),
                saved.getExternalUserProfileId(),
                saved.getChannel(),
                saved.getTitle(),
                saved.getMessage());
        eventPublisherPort.publish(event);

        return saved;
    }

    @Override
    @Transactional
    public Notification update(Long id, Notification notification) {
        Notification existing = notificationRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));

        // Update fields as needed, for now assuming full replacement or mapping handled
        // outside
        // But typically we merge. Im simply saving the passed object with ID ensured
        notification.setId(id);
        if (notification.getUpdatedAt() == null) {
            notification.setUpdatedAt(Instant.now());
        }
        return notificationRepositoryPort.save(notification);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        notificationRepositoryPort.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Notification getById(Long id) {
        return notificationRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> listByExternalUser(String externalUserProfileId) {
        return notificationRepositoryPort.findByExternalUserProfileId(externalUserProfileId);
    }
}
