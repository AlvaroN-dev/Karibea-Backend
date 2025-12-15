package com.microservice.identity.domain.port.in.auth;

public interface ValidateTokenUseCase {
    void validateToken(String token);
}
