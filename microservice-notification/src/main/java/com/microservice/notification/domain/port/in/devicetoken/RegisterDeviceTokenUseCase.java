package com.microservice.notification.domain.port.in.devicetoken;

import com.microservice.notification.domain.model.DeviceToken;

public interface RegisterDeviceTokenUseCase {
    DeviceToken register(DeviceToken deviceToken);
}
