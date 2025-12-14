package com.microservice.store.domain.port;

public interface UserGatewayPort {
    boolean exists(String userId);
}
