package com.microservice.notification.domain.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.microservice.notification.domain.model.DeviceToken;

public interface DeviceTokenRepositoryPort {

    DeviceToken save(DeviceToken deviceToken);

    Optional<DeviceToken> findById(UUID id);

    List<DeviceToken> findByExternalUserId(UUID externalUserId);

    void deleteById(UUID id);
}
