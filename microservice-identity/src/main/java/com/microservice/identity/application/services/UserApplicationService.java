package com.microservice.identity.application.services;

import com.microservice.identity.domain.models.User;
import com.microservice.identity.domain.port.in.user.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Application Service that implements all user-related use case interfaces.
 * This service acts as a facade that delegates to the actual use case
 * implementations,
 * providing a unified interface for the infrastructure layer (controllers).
 * 
 * Follows the Facade pattern and Dependency Inversion Principle.
 */
@Service
public class UserApplicationService implements
        RegisterUserUseCase,
        AuthenticateUserUseCase,
        GetUserByIdUseCase,
        GetUserByUsernameUseCase,
        GetUserByEmailUseCase,
        GetAllUsersUseCase,
        UpdateUserProfileUseCase,
        ChangePasswordUseCase,
        DeleteUserUseCase,
        VerifyEmailUseCase {

    private final RegisterUserUseCase registerUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final GetUserByUsernameUseCase getUserByUsernameUseCase;
    private final GetUserByEmailUseCase getUserByEmailUseCase;
    private final GetAllUsersUseCase getAllUsersUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final VerifyEmailUseCase verifyEmailUseCase;

    public UserApplicationService(
            RegisterUserUseCase registerUserUseCase,
            AuthenticateUserUseCase authenticateUserUseCase,
            GetUserByIdUseCase getUserByIdUseCase,
            GetUserByUsernameUseCase getUserByUsernameUseCase,
            GetUserByEmailUseCase getUserByEmailUseCase,
            GetAllUsersUseCase getAllUsersUseCase,
            UpdateUserProfileUseCase updateUserProfileUseCase,
            ChangePasswordUseCase changePasswordUseCase,
            DeleteUserUseCase deleteUserUseCase,
            VerifyEmailUseCase verifyEmailUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.authenticateUserUseCase = authenticateUserUseCase;
        this.getUserByIdUseCase = getUserByIdUseCase;
        this.getUserByUsernameUseCase = getUserByUsernameUseCase;
        this.getUserByEmailUseCase = getUserByEmailUseCase;
        this.getAllUsersUseCase = getAllUsersUseCase;
        this.updateUserProfileUseCase = updateUserProfileUseCase;
        this.changePasswordUseCase = changePasswordUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        this.verifyEmailUseCase = verifyEmailUseCase;
    }

    @Override
    public User registerUser(String username, String email, String password) {
        return registerUserUseCase.registerUser(username, email, password);
    }

    @Override
    public User authenticateUser(String username, String password) {
        return authenticateUserUseCase.authenticateUser(username, password);
    }

    @Override
    public void verifyEmail(UUID userId, String token) {
        verifyEmailUseCase.verifyEmail(userId, token);
    }

    @Override
    public User getUserById(UUID id) {
        return getUserByIdUseCase.getUserById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return getUserByUsernameUseCase.getUserByUsername(username);
    }

    @Override
    public User getUserByEmail(String email) {
        return getUserByEmailUseCase.getUserByEmail(email);
    }

    @Override
    public List<User> getUsers() {
        return getAllUsersUseCase.getUsers();
    }

    @Override
    public void updateUserProfile(UUID userId, String email, String name) {
        updateUserProfileUseCase.updateUserProfile(userId, email, name);
    }

    @Override
    public void changePassword(UUID userId, String currentPassword, String newPassword) {
        changePasswordUseCase.changePassword(userId, currentPassword, newPassword);
    }

    @Override
    public void deleteUser(UUID userId) {
        deleteUserUseCase.deleteUser(userId);
    }
}
