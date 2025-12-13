package com.microservice.shipping.infrastructure.controller;

import com.microservice.shipping.application.dto.request.*;
import com.microservice.shipping.application.dto.response.ShipmentResponse;
import com.microservice.shipping.application.exception.ShipmentNotFoundException;
import com.microservice.shipping.application.mapper.ShipmentMapper;
import com.microservice.shipping.domain.models.Shipment;
import com.microservice.shipping.domain.port.in.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for Shipment operations.
 */
@RestController
@RequestMapping("/api/v1/shipments")
@RequiredArgsConstructor
@Slf4j
public class ShipmentController {

    private final CreateShipmentPort createShipmentPort;
    private final GetShipmentPort getShipmentPort;
    private final UpdateShipmentStatusPort updateShipmentStatusPort;
    private final CancelShipmentPort cancelShipmentPort;
    private final AddTrackingEventPort addTrackingEventPort;
    private final ShipmentMapper shipmentMapper;

    /**
     * Create a new shipment.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShipmentResponse createShipment(
            @Valid @RequestBody CreateShipmentRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        log.info("Creating shipment for order: {}", request.orderId());

        Shipment shipment = createShipmentPort.execute(shipmentMapper.toCommand(request));

        return shipmentMapper.toResponse(shipment);
    }

    /**
     * Get shipment by ID.
     */
    @GetMapping("/{shipmentId}")
    public ShipmentResponse getShipmentById(@PathVariable UUID shipmentId) {
        log.debug("Getting shipment: {}", shipmentId);

        Shipment shipment = getShipmentPort.findById(shipmentId)
                .orElseThrow(() -> ShipmentNotFoundException.withId(shipmentId));

        return shipmentMapper.toResponse(shipment);
    }

    /**
     * Get shipment by tracking number.
     */
    @GetMapping("/tracking/{trackingNumber}")
    public ShipmentResponse getShipmentByTrackingNumber(@PathVariable String trackingNumber) {
        log.debug("Getting shipment by tracking number: {}", trackingNumber);

        Shipment shipment = getShipmentPort.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> ShipmentNotFoundException.withTrackingNumber(trackingNumber));

        return shipmentMapper.toResponse(shipment);
    }

    /**
     * Get shipments by order ID.
     */
    @GetMapping("/order/{orderId}")
    public Page<ShipmentResponse> getShipmentsByOrder(
            @PathVariable UUID orderId,
            @PageableDefault(size = 10) Pageable pageable) {

        log.debug("Getting shipments for order: {}", orderId);

        return getShipmentPort.findByOrderId(orderId, pageable)
                .map(shipmentMapper::toResponse);
    }

    /**
     * Get shipments by customer ID.
     */
    @GetMapping("/customer/{customerId}")
    public Page<ShipmentResponse> getShipmentsByCustomer(
            @PathVariable UUID customerId,
            @PageableDefault(size = 10) Pageable pageable) {

        log.debug("Getting shipments for customer: {}", customerId);

        return getShipmentPort.findByCustomerId(customerId, pageable)
                .map(shipmentMapper::toResponse);
    }

    /**
     * Update shipment status.
     */
    @PatchMapping("/{shipmentId}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable UUID shipmentId,
            @Valid @RequestBody UpdateShipmentStatusRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        log.info("Updating shipment {} status to: {}", shipmentId, request.newStatus());

        String updatedBy = jwt != null ? jwt.getSubject() : "system";

        updateShipmentStatusPort.execute(new UpdateShipmentStatusPort.UpdateStatusCommand(
                shipmentId,
                request.newStatus(),
                request.location(),
                request.reason(),
                updatedBy));

        return ResponseEntity.noContent().build();
    }

    /**
     * Cancel a shipment.
     */
    @PostMapping("/{shipmentId}/cancel")
    public ResponseEntity<Void> cancelShipment(
            @PathVariable UUID shipmentId,
            @Valid @RequestBody CancelShipmentRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        log.info("Cancelling shipment: {}", shipmentId);

        String cancelledBy = jwt != null ? jwt.getSubject() : "system";

        cancelShipmentPort.execute(new CancelShipmentPort.CancelShipmentCommand(
                shipmentId,
                request.reason(),
                cancelledBy));

        return ResponseEntity.noContent().build();
    }

    /**
     * Add tracking event.
     */
    @PostMapping("/{shipmentId}/tracking")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addTrackingEvent(
            @PathVariable UUID shipmentId,
            @Valid @RequestBody AddTrackingEventRequest request) {

        log.info("Adding tracking event to shipment: {}", shipmentId);

        addTrackingEventPort.execute(new AddTrackingEventPort.AddTrackingEventCommand(
                shipmentId,
                request.status(),
                request.location(),
                request.description(),
                request.occurredAt()));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
