package com.microservice.notification.infrastructure.controller;

import com.microservice.notification.application.dto.UserPreferencesRequest;
import com.microservice.notification.application.dto.UserPreferencesResponse;
import com.microservice.notification.application.mapper.NotificationDtoMapper;
import com.microservice.notification.domain.model.UserNotificationPreferences;
import com.microservice.notification.domain.port.in.usernotificationpreferences.CreateUserPreferencesUseCase;
import com.microservice.notification.domain.port.in.usernotificationpreferences.GetUserPreferencesUseCase;
import com.microservice.notification.domain.port.in.usernotificationpreferences.UpdateUserPreferencesUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/preferences")
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

    @PostMapping
    public ResponseEntity<UserPreferencesResponse> create(@RequestBody UserPreferencesRequest request) {
        UserNotificationPreferences domain = mapper.toDomain(request);
        UserNotificationPreferences created = createUserPreferencesUseCase.create(domain);
        return new ResponseEntity<>(mapper.toResponse(created), HttpStatus.CREATED);
    }

    @GetMapping("/{externalUserId}")
    public ResponseEntity<UserPreferencesResponse> get(@PathVariable String externalUserId) {
        UserNotificationPreferences preferences = getUserPreferencesUseCase.getByExternalUserId(externalUserId);
        if (preferences == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.toResponse(preferences));
    }

    @PutMapping("/{externalUserId}")
    public ResponseEntity<UserPreferencesResponse> update(@PathVariable String externalUserId,
            @RequestBody UserPreferencesRequest request) {
        UserNotificationPreferences domain = mapper.toDomain(request);
        UserNotificationPreferences updated = updateUserPreferencesUseCase.update(externalUserId, domain);
        return ResponseEntity.ok(mapper.toResponse(updated));
    }
}
