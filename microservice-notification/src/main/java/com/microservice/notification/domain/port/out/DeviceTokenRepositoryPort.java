package com.microservice.notification.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.microservice.notification.domain.model.DeviceToken;

public interface DeviceTokenRepositoryPort {

    DeviceToken save(DeviceToken deviceToken);

    Optional<DeviceToken> findById(Long id);

    List<DeviceToken> findByExternalUserId(String externalUserId);
}
