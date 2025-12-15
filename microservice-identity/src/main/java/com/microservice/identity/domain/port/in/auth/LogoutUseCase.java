package com.microservice.identity.domain.port.in.auth;

public interface LogoutUseCase {
    void logout(String token);
}
