package com.microservice.store.domain.port;

import java.util.UUID;

public interface UserGatewayPort {
    boolean exists(UUID userId);
}
