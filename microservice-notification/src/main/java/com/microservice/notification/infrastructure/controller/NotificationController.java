package com.microservice.notification.infrastructure.controller;

import com.microservice.notification.application.dto.NotificationRequest;
import com.microservice.notification.application.dto.NotificationResponse;
import com.microservice.notification.application.mapper.NotificationDtoMapper;
import com.microservice.notification.domain.model.Notification;
import com.microservice.notification.domain.port.in.notification.CreateNotificationUseCase;
import com.microservice.notification.domain.port.in.notification.GetNotificationUseCase;
import com.microservice.notification.domain.port.in.notification.ListNotificationsUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/notifications")
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

    @PostMapping
    public ResponseEntity<NotificationResponse> create(@RequestBody NotificationRequest request) {
        Notification domain = mapper.toDomain(request);
        Notification created = createNotificationUseCase.create(domain);
        return new ResponseEntity<>(mapper.toResponse(created), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getById(@PathVariable Long id) {
        Notification notification = getNotificationUseCase.getById(id);
        return ResponseEntity.ok(mapper.toResponse(notification));
    }

    @GetMapping("/user/{externalUserId}")
    public ResponseEntity<List<NotificationResponse>> listByUser(@PathVariable String externalUserId) {
        List<Notification> notifications = listNotificationsUseCase.listByExternalUser(externalUserId);
        List<NotificationResponse> response = notifications.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
