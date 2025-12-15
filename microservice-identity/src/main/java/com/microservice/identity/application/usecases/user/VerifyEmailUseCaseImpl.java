package com.microservice.identity.application.usecases.user;

import com.microservice.identity.domain.models.User;
import com.microservice.identity.domain.port.in.user.VerifyEmailUseCase;
import com.microservice.identity.domain.port.out.EventPublisherPort;
import com.microservice.identity.domain.port.out.UserRepositoryPort;

import java.util.UUID;

public class VerifyEmailUseCaseImpl implements VerifyEmailUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final EventPublisherPort eventPublisherPort;


    public VerifyEmailUseCaseImpl(UserRepositoryPort userRepositoryPort, EventPublisherPort eventPublisherPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.eventPublisherPort = eventPublisherPort;
    }

    @Override
    public void verifyEmail(UUID userId, String token) {
        User user = userRepositoryPort.findByVerificationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid verification token"));

        if (!user.getId().equals(userId)) {
            throw new IllegalArgumentException("Token does not match user");
        }

        user.verifyEmail();
        userRepositoryPort.save(user);
        eventPublisherPort.publishEmailVerified(userId);
    }




}
