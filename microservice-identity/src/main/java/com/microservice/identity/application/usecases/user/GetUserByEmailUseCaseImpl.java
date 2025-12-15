package com.microservice.identity.application.usecases.user;

import com.microservice.identity.domain.exceptions.UserNotFoundException;
import com.microservice.identity.domain.models.User;
import com.microservice.identity.domain.port.in.user.GetUserByEmailUseCase;
import com.microservice.identity.domain.port.out.UserRepositoryPort;
import org.springframework.stereotype.Component;

@Component
public class GetUserByEmailUseCaseImpl implements GetUserByEmailUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public GetUserByEmailUseCaseImpl(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }
}
