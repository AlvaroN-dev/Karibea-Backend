package com.microservice.user.infrastructure.controller;

import com.microservice.user.application.dto.request.UpdatePreferencesRequest;
import com.microservice.user.application.dto.response.UserPreferencesResponse;
import com.microservice.user.application.mapper.PreferencesDtoMapper;
import com.microservice.user.domain.exceptions.PreferencesNotFoundException;
import com.microservice.user.domain.models.UserPreferences;
import com.microservice.user.domain.port.in.ManagePreferencesUseCase;
import com.microservice.user.domain.port.in.ManagePreferencesUseCase.UpdatePreferencesCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for user preferences management.
 */
@RestController
@RequestMapping("/api/v1/preferences")
@Tag(name = "Preferences", description = "Operations for managing user preferences")
@SecurityRequirement(name = "bearerAuth")
public class PreferencesController {
    
    private final ManagePreferencesUseCase managePreferencesUseCase;
    private final PreferencesDtoMapper mapper;
    
    public PreferencesController(ManagePreferencesUseCase managePreferencesUseCase,
                                PreferencesDtoMapper mapper) {
        this.managePreferencesUseCase = managePreferencesUseCase;
        this.mapper = mapper;
    }
    
    @PostMapping("/{externalUserId}")
    @Operation(summary = "Create preferences", description = "Creates default preferences for a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Preferences created successfully",
            content = @Content(schema = @Schema(implementation = UserPreferencesResponse.class))),
        @ApiResponse(responseCode = "401", description = "Not authenticated"),
        @ApiResponse(responseCode = "409", description = "Preferences already exist")
    })
    public ResponseEntity<UserPreferencesResponse> createPreferences(
            @Parameter(description = "External user ID") @PathVariable UUID externalUserId) {
        UserPreferences preferences = managePreferencesUseCase.create(externalUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(preferences));
    }
    
    @GetMapping("/{externalUserId}")
    @Operation(summary = "Get preferences", description = "Retrieves preferences for a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Preferences found",
            content = @Content(schema = @Schema(implementation = UserPreferencesResponse.class))),
        @ApiResponse(responseCode = "401", description = "Not authenticated"),
        @ApiResponse(responseCode = "404", description = "Preferences not found")
    })
    public ResponseEntity<UserPreferencesResponse> getPreferencesByUser(
            @Parameter(description = "External user ID") @PathVariable UUID externalUserId) {
        UserPreferences preferences = managePreferencesUseCase.findByExternalUserId(externalUserId)
            .orElseThrow(() -> new PreferencesNotFoundException(externalUserId));
        return ResponseEntity.ok(mapper.toResponse(preferences));
    }
    
    @PutMapping
    @Operation(summary = "Update preferences", description = "Updates user preferences")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Preferences updated successfully",
            content = @Content(schema = @Schema(implementation = UserPreferencesResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Not authenticated"),
        @ApiResponse(responseCode = "404", description = "Preferences not found")
    })
    public ResponseEntity<UserPreferencesResponse> updatePreferences(
            @Valid @RequestBody UpdatePreferencesRequest request) {
        UpdatePreferencesCommand command = new UpdatePreferencesCommand(
            request.getExternalUserId(),
            request.getLanguageId(),
            request.getCurrencyId(),
            request.getNotificationEmail(),
            request.getNotificationPush()
        );
        
        UserPreferences preferences = managePreferencesUseCase.update(command);
        return ResponseEntity.ok(mapper.toResponse(preferences));
    }
    
    @DeleteMapping("/{externalUserId}")
    @Operation(summary = "Delete preferences", description = "Deletes user preferences")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Preferences deleted successfully"),
        @ApiResponse(responseCode = "401", description = "Not authenticated"),
        @ApiResponse(responseCode = "404", description = "Preferences not found")
    })
    public ResponseEntity<Void> deletePreferences(
            @Parameter(description = "External user ID") @PathVariable UUID externalUserId) {
        managePreferencesUseCase.delete(externalUserId);
        return ResponseEntity.noContent().build();
    }
}
