package com.microservice.notification.domain.port.in.devicetoken;

import java.util.UUID;

public interface DeactivateDeviceTokenUseCase {
    void deactivate(UUID id);
}
