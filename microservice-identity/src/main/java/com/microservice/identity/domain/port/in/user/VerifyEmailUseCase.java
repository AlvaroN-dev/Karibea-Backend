package com.microservice.identity.domain.port.in.user;

import java.util.UUID;

public interface VerifyEmailUseCase {
    void verifyEmail(UUID userId, String token);
}
