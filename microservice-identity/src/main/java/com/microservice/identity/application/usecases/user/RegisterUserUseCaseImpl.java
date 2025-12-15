package com.microservice.identity.application.usecases.user;

import com.microservice.identity.application.exception.ValidationException;
import com.microservice.identity.domain.models.User;
import com.microservice.identity.domain.port.in.user.RegisterUserUseCase;
import com.microservice.identity.domain.port.out.EmailServicePort;
import com.microservice.identity.domain.port.out.EventPublisherPort;
import com.microservice.identity.domain.port.out.PasswordEncoderPort;
import com.microservice.identity.domain.port.out.UserRepositoryPort;
import com.microservice.identity.domain.service.UserService;

public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final EmailServicePort emailServicePort;
    private final EventPublisherPort eventPublisherPort;
    private final PasswordEncoderPort  passwordEncoderPort;
    private final UserService userService;

    public RegisterUserUseCaseImpl(UserRepositoryPort userRepositoryPort, EmailServicePort emailServicePort, EventPublisherPort eventPublisherPort, PasswordEncoderPort passwordEncoderPort, UserService userService) {
        this.userRepositoryPort = userRepositoryPort;
        this.emailServicePort = emailServicePort;
        this.eventPublisherPort = eventPublisherPort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.userService = userService;
    }

    @Override
    public User registerUser(String username, String email, String password) {
        if (password.length() < 8) {
            throw new ValidationException("password", "Password must be at least 8 characters");
        }

        // Usar servicio de dominio
        userService.validateUserCreation(email, username);

        // Hash password
        String hashedPassword = passwordEncoderPort.encode(password);

        // Crear usuario (dominio)
        User user = User.create(username, email, hashedPassword);
        User savedUser = userRepositoryPort.save(user);

        // Enviar email
        emailServicePort.sendVerificationEmail(savedUser.getEmail(), savedUser.getVerificationToken());

        // Publicar evento
        eventPublisherPort.publishUserCreated(savedUser);

        return savedUser;
    }
}
