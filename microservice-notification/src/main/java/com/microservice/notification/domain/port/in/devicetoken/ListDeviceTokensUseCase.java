package com.microservice.notification.domain.port.in.devicetoken;

import java.util.List;
import java.util.UUID;

import com.microservice.notification.domain.model.DeviceToken;

public interface ListDeviceTokensUseCase {
    List<DeviceToken> listByExternalUserId(UUID externalUserId);
}
