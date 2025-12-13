package com.microservice.notification.infrastructure.adapters;

import com.microservice.notification.domain.model.NotificationTemplate;
import com.microservice.notification.domain.port.out.NotificationTemplateRepositoryPort;
import com.microservice.notification.infrastructure.adapters.mapper.NotificationMapper;
import com.microservice.notification.infrastructure.entities.NotificationTemplateEntity;
import com.microservice.notification.infrastructure.repositories.JpaNotificationTemplateRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class NotificationTemplateRepositoryAdapter implements NotificationTemplateRepositoryPort {

    private final JpaNotificationTemplateRepository jpaNotificationTemplateRepository;
    private final NotificationMapper mapper;

    public NotificationTemplateRepositoryAdapter(JpaNotificationTemplateRepository jpaNotificationTemplateRepository,
            NotificationMapper mapper) {
        this.jpaNotificationTemplateRepository = jpaNotificationTemplateRepository;
        this.mapper = mapper;
    }

    @Override
    public NotificationTemplate save(NotificationTemplate template) {
        NotificationTemplateEntity entity = mapper.toEntity(template);
        return mapper.toDomain(jpaNotificationTemplateRepository.save(entity));
    }

    @Override
    public Optional<NotificationTemplate> findById(Long id) {
        return jpaNotificationTemplateRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<NotificationTemplate> findActive() {
        return jpaNotificationTemplateRepository.findByIsActiveTrue().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaNotificationTemplateRepository.deleteById(id);
    }
}
