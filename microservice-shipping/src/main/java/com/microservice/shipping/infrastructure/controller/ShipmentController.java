package com.microservice.shipping.infrastructure.controller;

import com.microservice.shipping.application.dto.request.*;
import com.microservice.shipping.application.dto.response.ShipmentResponse;
import com.microservice.shipping.application.exception.ShipmentNotFoundException;
import com.microservice.shipping.application.mapper.ShipmentMapper;
import com.microservice.shipping.domain.models.Shipment;
import com.microservice.shipping.domain.port.in.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 * Provides endpoints for creating, retrieving, updating, and tracking shipments.
 */
@RestController
@RequestMapping("/api/v1/shipments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Shipments", description = "Shipment management and tracking operations")
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
    @Operation(
            summary = "Create a new shipment",
            description = "Creates a new shipment for an order. Typically triggered by order confirmation events."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Shipment created successfully",
                    content = @Content(schema = @Schema(implementation = ShipmentResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required"),
            @ApiResponse(responseCode = "422", description = "Domain validation error")
    })
    public ShipmentResponse createShipment(
            @Valid @RequestBody CreateShipmentRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        log.info("Creating shipment for order: {}", request.getOrderId());

        Shipment shipment = createShipmentPort.execute(shipmentMapper.toCommand(request));

        return shipmentMapper.toResponse(shipment);
    }

    /**
     * Get shipment by ID.
     */
    @GetMapping("/{shipmentId}")
    @Operation(
            summary = "Get shipment by ID",
            description = "Retrieves complete shipment information including tracking history and items."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Shipment found",
                    content = @Content(schema = @Schema(implementation = ShipmentResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Shipment not found")
    })
    public ShipmentResponse getShipmentById(
            @Parameter(description = "Unique shipment identifier", required = true)
            @PathVariable UUID shipmentId) {

        log.debug("Getting shipment: {}", shipmentId);

        Shipment shipment = getShipmentPort.findById(shipmentId)
                .orElseThrow(() -> ShipmentNotFoundException.withId(shipmentId));

        return shipmentMapper.toResponse(shipment);
    }

    /**
     * Get shipment by tracking number.
     */
    @GetMapping("/tracking/{trackingNumber}")
    @Operation(
            summary = "Get shipment by tracking number",
            description = "Public endpoint for customers to track their shipment using the tracking number."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Shipment found",
                    content = @Content(schema = @Schema(implementation = ShipmentResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Shipment not found with the given tracking number")
    })
    public ShipmentResponse getShipmentByTrackingNumber(
            @Parameter(description = "Tracking number (e.g., FEDEX-20241215-A1B2C3D4)", required = true)
            @PathVariable String trackingNumber) {

        log.debug("Getting shipment by tracking number: {}", trackingNumber);

        Shipment shipment = getShipmentPort.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> ShipmentNotFoundException.withTrackingNumber(trackingNumber));

        return shipmentMapper.toResponse(shipment);
    }

    /**
     * Get shipments by order ID.
     */
    @GetMapping("/order/{orderId}")
    @Operation(
            summary = "Get shipments by order",
            description = "Retrieves all shipments associated with a specific order. An order may have multiple shipments if split."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Shipments retrieved successfully"
            )
    })
    public Page<ShipmentResponse> getShipmentsByOrder(
            @Parameter(description = "Order identifier", required = true)
            @PathVariable UUID orderId,
            @Parameter(description = "Pagination parameters")
            @PageableDefault(size = 10) Pageable pageable) {

        log.debug("Getting shipments for order: {}", orderId);

        return getShipmentPort.findByOrderId(orderId, pageable)
                .map(shipmentMapper::toResponse);
    }

    /**
     * Get shipments by customer ID.
     */
    @GetMapping("/customer/{customerId}")
    @Operation(
            summary = "Get shipments by customer",
            description = "Retrieves all shipments for a specific customer, paginated and sorted."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Shipments retrieved successfully"
            )
    })
    public Page<ShipmentResponse> getShipmentsByCustomer(
            @Parameter(description = "Customer identifier", required = true)
            @PathVariable UUID customerId,
            @Parameter(description = "Pagination parameters")
            @PageableDefault(size = 10) Pageable pageable) {

        log.debug("Getting shipments for customer: {}", customerId);

        return getShipmentPort.findByCustomerId(customerId, pageable)
                .map(shipmentMapper::toResponse);
    }

    /**
     * Update shipment status.
     */
    @PatchMapping("/{shipmentId}/status")
    @Operation(
            summary = "Update shipment status",
            description = "Transitions the shipment to a new status. Valid transitions are enforced by the domain."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status transition"),
            @ApiResponse(responseCode = "404", description = "Shipment not found"),
            @ApiResponse(responseCode = "409", description = "Invalid state transition - cannot transition from current status")
    })
    public ResponseEntity<Void> updateStatus(
            @Parameter(description = "Shipment identifier", required = true)
            @PathVariable UUID shipmentId,
            @Valid @RequestBody UpdateShipmentStatusRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        log.info("Updating shipment {} status to: {}", shipmentId, request.getNewStatus());

        String updatedBy = jwt != null ? jwt.getSubject() : "system";

        updateShipmentStatusPort.execute(new UpdateShipmentStatusPort.UpdateStatusCommand(
                shipmentId,
                request.getNewStatus(),
                request.getLocation(),
                request.getReason(),
                updatedBy));

        return ResponseEntity.noContent().build();
    }

    /**
     * Cancel a shipment.
     */
    @PostMapping("/{shipmentId}/cancel")
    @Operation(
            summary = "Cancel a shipment",
            description = "Cancels a shipment. Only shipments in PENDING, CONFIRMED, or PICKED_UP status can be cancelled."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Shipment cancelled successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request - reason required"),
            @ApiResponse(responseCode = "404", description = "Shipment not found"),
            @ApiResponse(responseCode = "409", description = "Cannot cancel - shipment already delivered or in final state")
    })
    public ResponseEntity<Void> cancelShipment(
            @Parameter(description = "Shipment identifier", required = true)
            @PathVariable UUID shipmentId,
            @Valid @RequestBody CancelShipmentRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        log.info("Cancelling shipment: {}", shipmentId);

        String cancelledBy = jwt != null ? jwt.getSubject() : "system";

        cancelShipmentPort.execute(new CancelShipmentPort.CancelShipmentCommand(
                shipmentId,
                request.getReason(),
                cancelledBy));

        return ResponseEntity.noContent().build();
    }

    /**
     * Add tracking event.
     */
    @PostMapping("/{shipmentId}/tracking")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Add a tracking event",
            description = "Adds a new tracking event to the shipment's history. Used by carriers or internal systems to update shipment progress."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tracking event added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid tracking event data"),
            @ApiResponse(responseCode = "404", description = "Shipment not found")
    })
    public ResponseEntity<Void> addTrackingEvent(
            @Parameter(description = "Shipment identifier", required = true)
            @PathVariable UUID shipmentId,
            @Valid @RequestBody AddTrackingEventRequest request) {

        log.info("Adding tracking event to shipment: {}", shipmentId);

        addTrackingEventPort.execute(new AddTrackingEventPort.AddTrackingEventCommand(
                shipmentId,
                request.getStatus(),
                request.getLocation(),
                request.getDescription(),
                request.getOccurredAt()));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
