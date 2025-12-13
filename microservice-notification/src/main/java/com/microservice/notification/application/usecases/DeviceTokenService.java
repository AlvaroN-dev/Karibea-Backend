package com.microservice.notification.application.usecases;

import com.microservice.notification.domain.model.DeviceToken;
import com.microservice.notification.domain.port.in.devicetoken.DeactivateDeviceTokenUseCase;
import com.microservice.notification.domain.port.in.devicetoken.ListDeviceTokensUseCase;
import com.microservice.notification.domain.port.in.devicetoken.RegisterDeviceTokenUseCase;
import com.microservice.notification.domain.port.out.DeviceTokenRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class DeviceTokenService
        implements RegisterDeviceTokenUseCase, ListDeviceTokensUseCase, DeactivateDeviceTokenUseCase {

    private final DeviceTokenRepositoryPort deviceTokenRepositoryPort;

    public DeviceTokenService(DeviceTokenRepositoryPort deviceTokenRepositoryPort) {
        this.deviceTokenRepositoryPort = deviceTokenRepositoryPort;
    }

    @Override
    @Transactional
    public DeviceToken register(DeviceToken deviceToken) {
        if (deviceToken.getCreatedAt() == null) {
            deviceToken.setCreatedAt(Instant.now());
        }
        deviceToken.setActive(true);
        deviceToken.setLastUsedAt(Instant.now());
        return deviceTokenRepositoryPort.save(deviceToken);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceToken> listByExternalUserId(String externalUserId) {
        return deviceTokenRepositoryPort.findByExternalUserId(externalUserId);
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        Optional<DeviceToken> existing = deviceTokenRepositoryPort.findById(id);
        existing.ifPresent(token -> {
            token.setActive(false);
            token.setUpdatedAt(Instant.now());
            deviceTokenRepositoryPort.save(token);
        });
    }
}
