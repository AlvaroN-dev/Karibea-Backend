package com.microservice.notification.infrastructure.controller;

import com.microservice.notification.application.dto.NotificationRequest;
import com.microservice.notification.application.dto.NotificationResponse;
import com.microservice.notification.application.mapper.NotificationDtoMapper;
import com.microservice.notification.domain.model.Notification;
import com.microservice.notification.domain.port.in.notification.CreateNotificationUseCase;
import com.microservice.notification.domain.port.in.notification.GetNotificationUseCase;
import com.microservice.notification.domain.port.in.notification.ListNotificationsUseCase;
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
@RequestMapping("/api/v1/notifications")
@Tag(name = "Notifications", description = "API para gestión de notificaciones")
public class NotificationController {

    private final CreateNotificationUseCase createNotificationUseCase;
    private final GetNotificationUseCase getNotificationUseCase;
    private final ListNotificationsUseCase listNotificationsUseCase;
    private final NotificationDtoMapper mapper;

    public NotificationController(CreateNotificationUseCase createNotificationUseCase,
            GetNotificationUseCase getNotificationUseCase,
            ListNotificationsUseCase listNotificationsUseCase,
            NotificationDtoMapper mapper) {
        this.createNotificationUseCase = createNotificationUseCase;
        this.getNotificationUseCase = getNotificationUseCase;
        this.listNotificationsUseCase = listNotificationsUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "Crear una nueva notificación", 
               description = "Crea una nueva notificación y la encola para envío según el canal especificado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Notificación creada exitosamente",
                     content = @Content(schema = @Schema(implementation = NotificationResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<NotificationResponse> create(@RequestBody NotificationRequest request) {
        Notification domain = mapper.toDomain(request);
        Notification created = createNotificationUseCase.create(domain);
        return new ResponseEntity<>(mapper.toResponse(created), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener notificación por ID", 
               description = "Retorna la información completa de una notificación incluyendo status y template")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificación encontrada",
                     content = @Content(schema = @Schema(implementation = NotificationResponse.class))),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getById(
            @Parameter(description = "UUID de la notificación", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        Notification notification = getNotificationUseCase.getById(id);
        return ResponseEntity.ok(mapper.toResponse(notification));
    }

    @Operation(summary = "Listar notificaciones por usuario", 
               description = "Retorna todas las notificaciones de un usuario externo con información completa")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de notificaciones")
    })
    @GetMapping("/user/{externalUserId}")
    public ResponseEntity<List<NotificationResponse>> listByUser(
            @Parameter(description = "ID externo del usuario", example = "user-profile-123")
            @PathVariable String externalUserId) {
        List<Notification> notifications = listNotificationsUseCase.listByExternalUser(externalUserId);
        List<NotificationResponse> response = notifications.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
