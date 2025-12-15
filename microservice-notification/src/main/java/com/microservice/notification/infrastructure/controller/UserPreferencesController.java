package com.microservice.notification.infrastructure.controller;

import com.microservice.notification.application.dto.UserPreferencesRequest;
import com.microservice.notification.application.dto.UserPreferencesResponse;
import com.microservice.notification.application.mapper.NotificationDtoMapper;
import com.microservice.notification.domain.model.UserNotificationPreferences;
import com.microservice.notification.domain.port.in.usernotificationpreferences.CreateUserPreferencesUseCase;
import com.microservice.notification.domain.port.in.usernotificationpreferences.GetUserPreferencesUseCase;
import com.microservice.notification.domain.port.in.usernotificationpreferences.UpdateUserPreferencesUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/preferences")
@Tag(name = "User Preferences", description = "API para gestión de preferencias de notificación de usuarios")
public class UserPreferencesController {

    private final CreateUserPreferencesUseCase createUserPreferencesUseCase;
    private final GetUserPreferencesUseCase getUserPreferencesUseCase;
    private final UpdateUserPreferencesUseCase updateUserPreferencesUseCase;
    private final NotificationDtoMapper mapper;

    public UserPreferencesController(CreateUserPreferencesUseCase createUserPreferencesUseCase,
            GetUserPreferencesUseCase getUserPreferencesUseCase,
            UpdateUserPreferencesUseCase updateUserPreferencesUseCase,
            NotificationDtoMapper mapper) {
        this.createUserPreferencesUseCase = createUserPreferencesUseCase;
        this.getUserPreferencesUseCase = getUserPreferencesUseCase;
        this.updateUserPreferencesUseCase = updateUserPreferencesUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "Crear preferencias de usuario", 
               description = "Crea las preferencias de notificación para un nuevo usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Preferencias creadas exitosamente",
                     content = @Content(schema = @Schema(implementation = UserPreferencesResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "Ya existen preferencias para este usuario")
    })
    @PostMapping
    public ResponseEntity<UserPreferencesResponse> create(@RequestBody UserPreferencesRequest request) {
        UserNotificationPreferences domain = mapper.toDomain(request);
        UserNotificationPreferences created = createUserPreferencesUseCase.create(domain);
        return new ResponseEntity<>(mapper.toResponse(created), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener preferencias por usuario", 
               description = "Retorna las preferencias de notificación de un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Preferencias encontradas",
                     content = @Content(schema = @Schema(implementation = UserPreferencesResponse.class))),
        @ApiResponse(responseCode = "404", description = "Preferencias no encontradas")
    })
    @GetMapping("/{externalUserId}")
    public ResponseEntity<UserPreferencesResponse> get(
            @Parameter(description = "ID externo del usuario", example = "user-123")
            @PathVariable String externalUserId) {
        UserNotificationPreferences preferences = getUserPreferencesUseCase.getByExternalUserId(externalUserId);
        if (preferences == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.toResponse(preferences));
    }

    @Operation(summary = "Actualizar preferencias de usuario", 
               description = "Actualiza las preferencias de notificación de un usuario existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Preferencias actualizadas exitosamente",
                     content = @Content(schema = @Schema(implementation = UserPreferencesResponse.class))),
        @ApiResponse(responseCode = "404", description = "Preferencias no encontradas")
    })
    @PutMapping("/{externalUserId}")
    public ResponseEntity<UserPreferencesResponse> update(
            @Parameter(description = "ID externo del usuario", example = "user-123")
            @PathVariable String externalUserId,
            @RequestBody UserPreferencesRequest request) {
        UserNotificationPreferences domain = mapper.toDomain(request);
        UserNotificationPreferences updated = updateUserPreferencesUseCase.update(externalUserId, domain);
        return ResponseEntity.ok(mapper.toResponse(updated));
    }
}
