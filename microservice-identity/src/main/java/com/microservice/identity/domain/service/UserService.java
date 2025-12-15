package com.microservice.identity.domain.service;

import com.microservice.identity.domain.exceptions.EmailAlreadyExistsException;
import com.microservice.identity.domain.models.User;
import com.microservice.identity.domain.port.out.UserRepositoryPort;



public class UserService {

    private final UserRepositoryPort userRepositoryPort;

    public UserService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }


    public void validateUserCreation(String email, String username) {
        if (userRepositoryPort.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exists: " + email);
        }
        if (userRepositoryPort.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }
    }

    public void validateUserUpdate(User user, String newEmail, String newUsername) {
        if (user.getEmail().equals(newEmail)) {
            return;
        }

        // Validar que el nuevo email no exista
        if (userRepositoryPort.existsByEmail(newEmail)) {
            throw new EmailAlreadyExistsException("Email already exists: " + newEmail);
        }

        if (user.getUsername().equals(newUsername)) {
            return;
        }

        // Validar que el nuevo username no exista
        if (userRepositoryPort.existsByUsername(newUsername)) {
            throw new IllegalArgumentException("Username already exists: " + newUsername);
        }

    }


}