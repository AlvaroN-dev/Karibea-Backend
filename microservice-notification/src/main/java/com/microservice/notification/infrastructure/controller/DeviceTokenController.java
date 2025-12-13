package com.microservice.notification.infrastructure.controller;

import com.microservice.notification.application.dto.DeviceTokenRequest;
import com.microservice.notification.application.dto.DeviceTokenResponse;
import com.microservice.notification.application.mapper.NotificationDtoMapper;
import com.microservice.notification.domain.model.DeviceToken;
import com.microservice.notification.domain.port.in.devicetoken.DeactivateDeviceTokenUseCase;
import com.microservice.notification.domain.port.in.devicetoken.ListDeviceTokensUseCase;
import com.microservice.notification.domain.port.in.devicetoken.RegisterDeviceTokenUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/device-tokens")
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

    @PostMapping
    public ResponseEntity<DeviceTokenResponse> register(@RequestBody DeviceTokenRequest request) {
        DeviceToken domain = mapper.toDomain(request);
        DeviceToken registered = registerDeviceTokenUseCase.register(domain);
        return new ResponseEntity<>(mapper.toResponse(registered), HttpStatus.CREATED);
    }

    @GetMapping("/user/{externalUserId}")
    public ResponseEntity<List<DeviceTokenResponse>> listByUser(@PathVariable String externalUserId) {
        List<DeviceToken> tokens = listDeviceTokensUseCase.listByExternalUserId(externalUserId);
        List<DeviceTokenResponse> response = tokens.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        deactivateDeviceTokenUseCase.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
