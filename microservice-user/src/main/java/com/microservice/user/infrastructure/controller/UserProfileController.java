package com.microservice.user.infrastructure.controller;

import com.microservice.user.application.dto.request.CreateUserProfileRequest;
import com.microservice.user.application.dto.request.UpdateUserProfileRequest;
import com.microservice.user.application.dto.response.PagedResponse;
import com.microservice.user.application.dto.response.UserProfileResponse;
import com.microservice.user.application.services.UserProfileApplicationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controlador REST para perfiles de usuario
 */
@RestController
@RequestMapping("/api/v1/profiles")
public class UserProfileController {
    
    private final UserProfileApplicationService userProfileService;
    
    public UserProfileController(UserProfileApplicationService userProfileService) {
        this.userProfileService = userProfileService;
    }
    
    @PostMapping
    public ResponseEntity<UserProfileResponse> createProfile(
            @Valid @RequestBody CreateUserProfileRequest request) {
        UserProfileResponse response = userProfileService.createProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getProfileById(@PathVariable UUID id) {
        UserProfileResponse response = userProfileService.getProfileById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/user/{externalUserId}")
    public ResponseEntity<UserProfileResponse> getProfileByExternalUserId(
            @PathVariable UUID externalUserId) {
        UserProfileResponse response = userProfileService.getProfileByExternalUserId(externalUserId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<PagedResponse<UserProfileResponse>> getAllProfiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("asc") 
            ? Sort.by(sortBy).ascending() 
            : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        PagedResponse<UserProfileResponse> response = userProfileService.getAllProfiles(pageable);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserProfileResponse> updateProfile(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserProfileRequest request) {
        UserProfileResponse response = userProfileService.updateProfile(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable UUID id) {
        userProfileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }
}
