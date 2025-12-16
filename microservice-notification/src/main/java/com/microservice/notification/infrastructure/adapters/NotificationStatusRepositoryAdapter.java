package com.microservice.notification.infrastructure.adapters;

import com.microservice.notification.domain.model.NotificationStatus;
import com.microservice.notification.domain.port.out.NotificationStatusRepositoryPort;
import com.microservice.notification.infrastructure.adapters.mapper.NotificationStatusMapper;
import com.microservice.notification.infrastructure.repositories.JpaNotificationStatusRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class NotificationStatusRepositoryAdapter implements NotificationStatusRepositoryPort {

    private final JpaNotificationStatusRepository jpaNotificationStatusRepository;
    private final NotificationStatusMapper mapper;

    public NotificationStatusRepositoryAdapter(JpaNotificationStatusRepository jpaNotificationStatusRepository,
            NotificationStatusMapper mapper) {
        this.jpaNotificationStatusRepository = jpaNotificationStatusRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<NotificationStatus> findById(UUID id) {
        return jpaNotificationStatusRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<NotificationStatus> findAll() {
        return jpaNotificationStatusRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
