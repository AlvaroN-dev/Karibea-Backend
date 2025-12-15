package com.microservice.notification.infrastructure.controller;

import com.microservice.notification.application.dto.DeviceTokenRequest;
import com.microservice.notification.application.dto.DeviceTokenResponse;
import com.microservice.notification.application.mapper.NotificationDtoMapper;
import com.microservice.notification.domain.model.DeviceToken;
import com.microservice.notification.domain.port.in.devicetoken.DeactivateDeviceTokenUseCase;
import com.microservice.notification.domain.port.in.devicetoken.ListDeviceTokensUseCase;
import com.microservice.notification.domain.port.in.devicetoken.RegisterDeviceTokenUseCase;
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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/device-tokens")
@Tag(name = "Device Tokens", description = "API para gestión de tokens de dispositivos para push notifications")
public class DeviceTokenController {

    private final RegisterDeviceTokenUseCase registerDeviceTokenUseCase;
    private final ListDeviceTokensUseCase listDeviceTokensUseCase;
    private final DeactivateDeviceTokenUseCase deactivateDeviceTokenUseCase;
    private final NotificationDtoMapper mapper;

    public DeviceTokenController(RegisterDeviceTokenUseCase registerDeviceTokenUseCase,
            ListDeviceTokensUseCase listDeviceTokensUseCase,
            DeactivateDeviceTokenUseCase deactivateDeviceTokenUseCase,
            NotificationDtoMapper mapper) {
        this.registerDeviceTokenUseCase = registerDeviceTokenUseCase;
        this.listDeviceTokensUseCase = listDeviceTokensUseCase;
        this.deactivateDeviceTokenUseCase = deactivateDeviceTokenUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "Registrar token de dispositivo", 
               description = "Registra un nuevo token de dispositivo para recibir push notifications")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Token registrado exitosamente",
                     content = @Content(schema = @Schema(implementation = DeviceTokenResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<DeviceTokenResponse> register(@RequestBody DeviceTokenRequest request) {
        DeviceToken domain = mapper.toDomain(request);
        DeviceToken registered = registerDeviceTokenUseCase.register(domain);
        return new ResponseEntity<>(mapper.toResponse(registered), HttpStatus.CREATED);
    }

    @Operation(summary = "Listar tokens por usuario", 
               description = "Retorna todos los tokens de dispositivo activos de un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tokens de dispositivo")
    })
    @GetMapping("/user/{externalUserId}")
    public ResponseEntity<List<DeviceTokenResponse>> listByUser(
            @Parameter(description = "ID externo del usuario", example = "user-123")
            @PathVariable UUID externalUserId) {
        List<DeviceToken> tokens = listDeviceTokensUseCase.listByExternalUserId(externalUserId);
        List<DeviceTokenResponse> response = tokens.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Desactivar token de dispositivo", 
               description = "Desactiva un token de dispositivo para que no reciba más push notifications")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Token desactivado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Token no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(
            @Parameter(description = "UUID del token", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        deactivateDeviceTokenUseCase.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
