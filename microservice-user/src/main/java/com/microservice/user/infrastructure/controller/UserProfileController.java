package com.microservice.user.infrastructure.controller;

import com.microservice.user.application.dto.request.CreateUserProfileRequest;
import com.microservice.user.application.dto.request.UpdateUserProfileRequest;
import com.microservice.user.application.dto.response.PagedResponse;
import com.microservice.user.application.dto.response.UserProfileResponse;
import com.microservice.user.application.services.UserProfileApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for user profile management.
 */
@RestController
@RequestMapping("/api/v1/profiles")
@Tag(name = "User Profiles", description = "Operations for managing user profiles")
@SecurityRequirement(name = "bearerAuth")
public class UserProfileController {
    
    private final UserProfileApplicationService userProfileService;
    
    public UserProfileController(UserProfileApplicationService userProfileService) {
        this.userProfileService = userProfileService;
    }
    
    @PostMapping
    @Operation(summary = "Create user profile", description = "Creates a new profile for an authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Profile created successfully",
            content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Not authenticated"),
        @ApiResponse(responseCode = "409", description = "Profile already exists for this user")
    })
    public ResponseEntity<UserProfileResponse> createProfile(
            @Valid @RequestBody CreateUserProfileRequest request) {
        UserProfileResponse response = userProfileService.createProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get profile by ID", description = "Retrieves a user profile by its internal ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile found",
            content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
        @ApiResponse(responseCode = "401", description = "Not authenticated"),
        @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<UserProfileResponse> getProfileById(
            @Parameter(description = "Profile ID") @PathVariable UUID id) {
        UserProfileResponse response = userProfileService.getProfileById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/user/{externalUserId}")
    @Operation(summary = "Get profile by external user ID", description = "Retrieves a user profile by the identity service user ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile found",
            content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
        @ApiResponse(responseCode = "401", description = "Not authenticated"),
        @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<UserProfileResponse> getProfileByExternalUserId(
            @Parameter(description = "External user ID from identity service") @PathVariable UUID externalUserId) {
        UserProfileResponse response = userProfileService.getProfileByExternalUserId(externalUserId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "Get all profiles", description = "Retrieves all user profiles with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profiles retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    public ResponseEntity<PagedResponse<UserProfileResponse>> getAllProfiles(
            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)") @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("asc") 
            ? Sort.by(sortBy).ascending() 
            : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        PagedResponse<UserProfileResponse> response = userProfileService.getAllProfiles(pageable);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update profile", description = "Updates an existing user profile")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile updated successfully",
            content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Not authenticated"),
        @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<UserProfileResponse> updateProfile(
            @Parameter(description = "Profile ID") @PathVariable UUID id,
            @Valid @RequestBody UpdateUserProfileRequest request) {
        UserProfileResponse response = userProfileService.updateProfile(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete profile", description = "Deletes a user profile")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Profile deleted successfully"),
        @ApiResponse(responseCode = "401", description = "Not authenticated"),
        @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<Void> deleteProfile(
            @Parameter(description = "Profile ID") @PathVariable UUID id) {
        userProfileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }
}
