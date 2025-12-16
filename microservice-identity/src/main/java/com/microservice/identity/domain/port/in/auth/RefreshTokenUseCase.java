package com.microservice.identity.domain.port.in.auth;

public interface RefreshTokenUseCase {
    String refreshToken(String refreshToken);
}
