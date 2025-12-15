package com.microservice.notification.infrastructure.adapters;

import com.microservice.notification.domain.model.DeviceToken;
import com.microservice.notification.domain.port.out.DeviceTokenRepositoryPort;
import com.microservice.notification.infrastructure.adapters.mapper.NotificationMapper;
import com.microservice.notification.infrastructure.entities.DeviceTokenEntity;
import com.microservice.notification.infrastructure.repositories.JpaDeviceTokenRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class DeviceTokenRepositoryAdapter implements DeviceTokenRepositoryPort {

    private final JpaDeviceTokenRepository jpaDeviceTokenRepository;
    private final NotificationMapper mapper;

    public DeviceTokenRepositoryAdapter(JpaDeviceTokenRepository jpaDeviceTokenRepository, NotificationMapper mapper) {
        this.jpaDeviceTokenRepository = jpaDeviceTokenRepository;
        this.mapper = mapper;
    }

    @Override
    public DeviceToken save(DeviceToken deviceToken) {
        DeviceTokenEntity entity = mapper.toEntity(deviceToken);
        return mapper.toDomain(jpaDeviceTokenRepository.save(entity));
    }

    @Override
    public Optional<DeviceToken> findById(UUID id) {
        return jpaDeviceTokenRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<DeviceToken> findByExternalUserId(UUID externalUserId) {
        return jpaDeviceTokenRepository.findByExternalUserId(externalUserId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {           
        jpaDeviceTokenRepository.deleteById(id);
    }
}
