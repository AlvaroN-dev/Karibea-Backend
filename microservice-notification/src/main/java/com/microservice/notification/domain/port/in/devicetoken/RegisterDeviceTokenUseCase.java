package com.microservice.notification.domain.port.in.devicetoken;

import com.microservice.notification.domain.model.DeviceToken;

/**
 * Use case for registering a new device token for push notifications.
 */
public interface RegisterDeviceTokenUseCase {

    /**
     * Registers a new device token.
     *
     * @param deviceToken the device token to register
     * @return the registered device token
     */
    DeviceToken register(DeviceToken deviceToken);
}
