package com.microservice.user.infrastructure.controller;

import com.microservice.user.application.dto.request.UpdatePreferencesRequest;
import com.microservice.user.application.dto.response.UserPreferencesResponse;
import com.microservice.user.application.mapper.PreferencesDtoMapper;
import com.microservice.user.domain.exceptions.PreferencesNotFoundException;
import com.microservice.user.domain.models.UserPreferences;
import com.microservice.user.domain.port.in.ManagePreferencesUseCase;
import com.microservice.user.domain.port.in.ManagePreferencesUseCase.UpdatePreferencesCommand;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controlador REST para preferencias del usuario
 */
@RestController
@RequestMapping("/api/v1/preferences")
public class PreferencesController {
    
    private final ManagePreferencesUseCase managePreferencesUseCase;
    private final PreferencesDtoMapper mapper;
    
    public PreferencesController(ManagePreferencesUseCase managePreferencesUseCase,
                                PreferencesDtoMapper mapper) {
        this.managePreferencesUseCase = managePreferencesUseCase;
        this.mapper = mapper;
    }
    
    @PostMapping("/{externalUserId}")
    public ResponseEntity<UserPreferencesResponse> createPreferences(
            @PathVariable UUID externalUserId) {
        UserPreferences preferences = managePreferencesUseCase.create(externalUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(preferences));
    }
    
    @GetMapping("/{externalUserId}")
    public ResponseEntity<UserPreferencesResponse> getPreferencesByUser(
            @PathVariable UUID externalUserId) {
        UserPreferences preferences = managePreferencesUseCase.findByExternalUserId(externalUserId)
            .orElseThrow(() -> new PreferencesNotFoundException(externalUserId));
        return ResponseEntity.ok(mapper.toResponse(preferences));
    }
    
    @PutMapping
    public ResponseEntity<UserPreferencesResponse> updatePreferences(
            @Valid @RequestBody UpdatePreferencesRequest request) {
        UpdatePreferencesCommand command = new UpdatePreferencesCommand(
            request.externalUserId(),
            request.languageId(),
            request.currencyId(),
            request.notificationEmail(),
            request.notificationPush()
        );
        
        UserPreferences preferences = managePreferencesUseCase.update(command);
        return ResponseEntity.ok(mapper.toResponse(preferences));
    }
    
    @DeleteMapping("/{externalUserId}")
    public ResponseEntity<Void> deletePreferences(@PathVariable UUID externalUserId) {
        managePreferencesUseCase.delete(externalUserId);
        return ResponseEntity.noContent().build();
    }
}
