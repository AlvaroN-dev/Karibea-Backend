package com.microservice.identity.infrastructure.controller;

import com.microservice.identity.application.dto.request.CreateUserRequest;
import com.microservice.identity.application.dto.request.UpdateUserRequest;
import com.microservice.identity.application.dto.response.UserResponse;
import com.microservice.identity.application.mapper.UserMapper;
import com.microservice.identity.application.services.UserApplicationService;
import com.microservice.identity.domain.models.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for User management operations.
 * Follows hexagonal architecture by depending on application services.
 * Implements best practices including soft delete pattern.
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "User management endpoints")
public class UserController {

    private final UserApplicationService userApplicationService;

    public UserController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @Operation(summary = "Register a new user", description = "Creates a new user account with the provided credentials. "
            +
            "Password will be securely hashed before storage.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully created", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data (e.g., invalid email format, weak password)"),
            @ApiResponse(responseCode = "409", description = "User already exists with the given username or email")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User registration data", required = true) @Valid @RequestBody CreateUserRequest request) {

        User user = userApplicationService.registerUser(
                request.getUsername(),
                request.getEmail(),
                request.getPassword());

        UserResponse response = UserMapper.toResponse(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get user by ID", description = "Retrieves a user by their unique identifier. " +
            "Returns 404 if user not found or has been soft-deleted.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found and returned successfully", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found or has been deleted")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "User unique identifier (UUID)", required = true, example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID id) {

        User user = userApplicationService.getUserById(id);
        return ResponseEntity.ok(UserMapper.toResponse(user));
    }

    @Operation(summary = "Get user by username", description = "Retrieves a user by their username. Case-sensitive search.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(
            @Parameter(description = "Username to search for", required = true, example = "johndoe") @PathVariable String username) {

        User user = userApplicationService.getUserByUsername(username);
        return ResponseEntity.ok(UserMapper.toResponse(user));
    }

    @Operation(summary = "Get all users", description = "Retrieves a list of all registered users. " +
            "Excludes soft-deleted users by default.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users retrieved successfully", content = @Content(schema = @Schema(implementation = UserResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userApplicationService.getUsers();
        return ResponseEntity.ok(UserMapper.toResponseList(users));
    }

    @Operation(summary = "Update user profile", description = "Updates user profile information (email and name). " +
            "Only provided fields will be updated.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "Email already in use by another user")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUserProfile(
            @Parameter(description = "User ID to update", required = true) @PathVariable UUID id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated user profile data") @Valid @RequestBody UpdateUserRequest request) {

        userApplicationService.updateUserProfile(id, request.getEmail(), request.getName());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Soft delete user", description = "Performs a soft delete on the user account. " +
            "The user is marked as deleted but data is retained in the database. " +
            "User will be disabled and cannot login after soft delete. " +
            "This operation is reversible by database administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User soft-deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "User is already deleted")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID to soft delete", required = true) @PathVariable UUID id) {

        // This performs a SOFT DELETE - user is marked as deleted but not removed from
        // database
        userApplicationService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Verify user email", description = "Verifies a user's email address using the verification token sent via email. "
            +
            "Once verified, the user's emailVerified flag is set to true.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email verified successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired verification token"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/verify-email")
    public ResponseEntity<Void> verifyEmail(
            @Parameter(description = "User ID", required = true) @RequestParam UUID userId,
            @Parameter(description = "Email verification token", required = true) @RequestParam String token) {

        userApplicationService.verifyEmail(userId, token);
        return ResponseEntity.ok().build();
    }
}
