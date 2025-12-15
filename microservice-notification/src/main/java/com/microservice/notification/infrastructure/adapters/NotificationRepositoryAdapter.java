package com.microservice.notification.infrastructure.adapters;

import com.microservice.notification.domain.model.Notification;
import com.microservice.notification.domain.port.out.NotificationRepositoryPort;
import com.microservice.notification.infrastructure.adapters.mapper.NotificationMapper;
import com.microservice.notification.infrastructure.entities.NotificationEntity;
import com.microservice.notification.infrastructure.repositories.JpaNotificationRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class NotificationRepositoryAdapter implements NotificationRepositoryPort {

    private final JpaNotificationRepository jpaNotificationRepository;
    private final NotificationMapper mapper;

    public NotificationRepositoryAdapter(JpaNotificationRepository jpaNotificationRepository,
            NotificationMapper mapper) {
        this.jpaNotificationRepository = jpaNotificationRepository;
        this.mapper = mapper;
    }

    @Override
    public Notification save(Notification notification) {
        NotificationEntity entity = mapper.toEntity(notification);
        return mapper.toDomain(jpaNotificationRepository.save(entity));
    }

    @Override
    public Optional<Notification> findById(UUID id) {
        return jpaNotificationRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Notification> findByExternalUserProfileId(String externalUserProfileId) {
        return jpaNotificationRepository.findByExternalUserProfileId(externalUserProfileId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaNotificationRepository.deleteById(id);
    }
}
