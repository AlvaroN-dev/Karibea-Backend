package com.microservice.inventory.infrastructure.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.microservice.inventory.application.dto.*;
import com.microservice.inventory.application.mapper.ReservationMapper;
import com.microservice.inventory.application.mapper.StockMapper;
import com.microservice.inventory.domain.models.Stock;
import com.microservice.inventory.domain.models.StockReservation;
import com.microservice.inventory.domain.port.in.*;
import com.microservice.inventory.infrastructure.exceptions.advice.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * REST Controller for Stock Management.
 * Handles inventory operations including stock creation, adjustments, and reservations.
 */
@RestController
@RequestMapping("/api/v1/stock")
@Validated
@Tag(name = "Stock Management", description = "Endpoints for managing inventory, stock levels, and reservations")
@SecurityRequirement(name = "bearer-key")
public class StockController {

        private final CreateStockUseCase createStockUseCase;
        private final AdjustStockUseCase adjustStockUseCase;
        private final ReserveStockUseCase reserveStockUseCase;
        private final ReleaseReservationUseCase releaseReservationUseCase;
        private final ConfirmReservationUseCase confirmReservationUseCase;
        private final GetStockAvailabilityUseCase getStockAvailabilityUseCase;
        private final StockMapper stockMapper;
        private final ReservationMapper reservationMapper;

        public StockController(
                        CreateStockUseCase createStockUseCase,
                        AdjustStockUseCase adjustStockUseCase,
                        ReserveStockUseCase reserveStockUseCase,
                        ReleaseReservationUseCase releaseReservationUseCase,
                        ConfirmReservationUseCase confirmReservationUseCase,
                        GetStockAvailabilityUseCase getStockAvailabilityUseCase,
                        StockMapper stockMapper,
                        ReservationMapper reservationMapper) {
                this.createStockUseCase = createStockUseCase;
                this.adjustStockUseCase = adjustStockUseCase;
                this.reserveStockUseCase = reserveStockUseCase;
                this.releaseReservationUseCase = releaseReservationUseCase;
                this.confirmReservationUseCase = confirmReservationUseCase;
                this.getStockAvailabilityUseCase = getStockAvailabilityUseCase;
                this.stockMapper = stockMapper;
                this.reservationMapper = reservationMapper;
        }

        @PostMapping
        @PreAuthorize("hasAuthority('SCOPE_inventory:write')")
        @Operation(
                summary = "Initialize Stock", 
                description = "Creates a new stock entry for a product variant in a warehouse. " +
                              "This endpoint initializes inventory tracking for a specific product/variant combination."
        )
        @ApiResponses({
                @ApiResponse(
                        responseCode = "201", 
                        description = "Stock created successfully", 
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = StockResponse.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "400", 
                        description = "Invalid input data - validation errors", 
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponse.class)
                        )
                ),
                @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
                @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions (requires inventory:write scope)")
        })
        public ResponseEntity<StockResponse> createStock(
                @Parameter(description = "Stock creation request", required = true)
                @Valid @RequestBody CreateStockRequest request) {
                Stock stock = createStockUseCase.execute(new CreateStockUseCase.CreateStockCommand(
                                request.getExternalProductId(),
                                request.getExternalVariantId(),
                                request.getWarehouseId(),
                                request.getInitialQuantity(),
                                request.getLowStockThreshold(),
                                request.getReorderPoint()));
                return ResponseEntity.status(HttpStatus.CREATED).body(stockMapper.toResponse(stock));
        }

        @GetMapping("/{stockId}")
        @PreAuthorize("hasAuthority('SCOPE_inventory:read')")
        @Operation(
                summary = "Get Stock Details", 
                description = "Retrieves detailed information about a specific stock record by its ID."
        )
        @ApiResponses({
                @ApiResponse(
                        responseCode = "200", 
                        description = "Stock found", 
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = StockResponse.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "404", 
                        description = "Stock not found", 
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponse.class)
                        )
                ),
                @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid")
        })
        public ResponseEntity<StockResponse> getStock(
                @Parameter(description = "Unique identifier of the stock record", required = true, example = "123e4567-e89b-12d3-a456-426614174099")
                @PathVariable UUID stockId) {
                Stock stock = getStockAvailabilityUseCase.getById(stockId);
                return ResponseEntity.ok(stockMapper.toResponse(stock));
        }

        @GetMapping("/variant/{variantId}")
        @PreAuthorize("hasAuthority('SCOPE_inventory:read')")
        @Operation(
                summary = "Get Stock by Variant", 
                description = "Retrieves all stock records for a given product variant across all warehouses. " +
                              "Useful for checking total availability across locations."
        )
        @ApiResponses({
                @ApiResponse(
                        responseCode = "200", 
                        description = "Stock records found", 
                        content = @Content(
                                mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = StockResponse.class))
                        )
                ),
                @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid")
        })
        public ResponseEntity<List<StockResponse>> getStockByVariant(
                @Parameter(description = "Unique identifier of the product variant", required = true, example = "123e4567-e89b-12d3-a456-426614174001")
                @PathVariable UUID variantId) {
                List<Stock> stocks = getStockAvailabilityUseCase.getByVariant(variantId);
                return ResponseEntity.ok(stocks.stream().map(stockMapper::toResponse).toList());
        }

        @GetMapping("/warehouse/{warehouseId}")
        @PreAuthorize("hasAuthority('SCOPE_inventory:read')")
        @Operation(
                summary = "Get Stock by Warehouse", 
                description = "Retrieves all stock records in a specific warehouse."
        )
        @ApiResponses({
                @ApiResponse(
                        responseCode = "200", 
                        description = "Stock records found", 
                        content = @Content(
                                mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = StockResponse.class))
                        )
                ),
                @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid")
        })
        public ResponseEntity<List<StockResponse>> getStockByWarehouse(
                @Parameter(description = "Unique identifier of the warehouse", required = true, example = "123e4567-e89b-12d3-a456-426614174002")
                @PathVariable UUID warehouseId) {
                List<Stock> stocks = getStockAvailabilityUseCase.getByWarehouse(warehouseId);
                return ResponseEntity.ok(stocks.stream().map(stockMapper::toResponse).toList());
        }

        @GetMapping("/warehouse/{warehouseId}/low-stock")
        @PreAuthorize("hasAuthority('SCOPE_inventory:read')")
        @Operation(
                summary = "Get Low Stock Items", 
                description = "Retrieves items that are below their low stock threshold in a specific warehouse. " +
                              "Useful for inventory replenishment planning."
        )
        @ApiResponses({
                @ApiResponse(
                        responseCode = "200", 
                        description = "Low stock items found", 
                        content = @Content(
                                mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = StockResponse.class))
                        )
                ),
                @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid")
        })
        public ResponseEntity<List<StockResponse>> getLowStock(
                @Parameter(description = "Unique identifier of the warehouse", required = true, example = "123e4567-e89b-12d3-a456-426614174002")
                @PathVariable UUID warehouseId) {
                List<Stock> stocks = getStockAvailabilityUseCase.getLowStockItems(warehouseId);
                return ResponseEntity.ok(stocks.stream().map(stockMapper::toResponse).toList());
        }

        @PostMapping("/adjust")
        @PreAuthorize("hasAuthority('SCOPE_inventory:write')")
        @Operation(
                summary = "Adjust Stock", 
                description = "Manually increases or decreases stock quantity. Use for purchases, returns, " +
                              "damages, or manual corrections. Creates a movement record for audit trail."
        )
        @ApiResponses({
                @ApiResponse(
                        responseCode = "200", 
                        description = "Stock adjusted successfully", 
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = StockResponse.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "404", 
                        description = "Stock not found", 
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponse.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "422", 
                        description = "Insufficient stock for decrease operation", 
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponse.class)
                        )
                ),
                @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
                @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
        })
        public ResponseEntity<StockResponse> adjustStock(
                @Parameter(description = "Stock adjustment request", required = true)
                @Valid @RequestBody AdjustStockRequest request,
                @AuthenticationPrincipal Jwt jwt) {
                UUID performedById = UUID.fromString(jwt.getSubject());
                Stock stock = adjustStockUseCase.execute(new AdjustStockUseCase.AdjustStockCommand(
                                request.getStockId(),
                                request.getMovementType(),
                                request.getQuantity(),
                                request.getReferenceType(),
                                request.getExternalReferenceId(),
                                performedById,
                                request.getNote()));
                return ResponseEntity.ok(stockMapper.toResponse(stock));
        }

        @PostMapping("/reserve")
        @PreAuthorize("hasAuthority('SCOPE_inventory:write')")
        @Operation(
                summary = "Reserve Stock", 
                description = "Reserves stock for an order or cart. The reserved quantity is deducted from " +
                              "available stock but can be released if the reservation expires or is cancelled."
        )
        @ApiResponses({
                @ApiResponse(
                        responseCode = "201", 
                        description = "Stock reserved successfully", 
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ReservationResponse.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "404", 
                        description = "Stock not found", 
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponse.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "422", 
                        description = "Insufficient stock available for reservation", 
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponse.class)
                        )
                ),
                @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
                @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
        })
        public ResponseEntity<ReservationResponse> reserveStock(
                @Parameter(description = "Stock reservation request", required = true)
                @Valid @RequestBody ReserveStockRequest request) {
                StockReservation reservation = reserveStockUseCase.execute(
                                new ReserveStockUseCase.ReserveStockCommand(
                                                request.getStockId(),
                                                request.getQuantity(),
                                                request.getReservationType(),
                                                request.getExternalCartId(),
                                                request.getExternalOrderId(),
                                                request.getExpiresAt()));
                return ResponseEntity.status(HttpStatus.CREATED).body(reservationMapper.toResponse(reservation));
        }

        @PostMapping("/reservations/{reservationId}/release")
        @PreAuthorize("hasAuthority('SCOPE_inventory:write')")
        @Operation(
                summary = "Release Reservation", 
                description = "Releases a stock reservation, making the quantity available again. " +
                              "Use when an order is cancelled or a cart expires."
        )
        @ApiResponses({
                @ApiResponse(responseCode = "204", description = "Reservation released successfully"),
                @ApiResponse(
                        responseCode = "404", 
                        description = "Reservation not found", 
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponse.class)
                        )
                ),
                @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
                @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
        })
        public ResponseEntity<Void> releaseReservation(
                @Parameter(description = "Unique identifier of the reservation", required = true, example = "123e4567-e89b-12d3-a456-426614174555")
                @PathVariable UUID reservationId,
                @Parameter(description = "Reason for releasing the reservation", example = "Order cancelled by customer")
                @RequestParam(required = false) String reason) {
                releaseReservationUseCase.execute(
                                new ReleaseReservationUseCase.ReleaseReservationCommand(reservationId, reason));
                return ResponseEntity.noContent().build();
        }

        @PostMapping("/reservations/{reservationId}/confirm")
        @PreAuthorize("hasAuthority('SCOPE_inventory:write')")
        @Operation(
                summary = "Confirm Reservation", 
                description = "Confirms a reservation (e.g., after payment success). This converts the reserved " +
                              "quantity to a confirmed sale and creates appropriate stock movements."
        )
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "Reservation confirmed successfully"),
                @ApiResponse(
                        responseCode = "404", 
                        description = "Reservation not found", 
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponse.class)
                        )
                ),
                @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
                @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
        })
        public ResponseEntity<Void> confirmReservation(
                @Parameter(description = "Unique identifier of the reservation", required = true, example = "123e4567-e89b-12d3-a456-426614174555")
                @PathVariable UUID reservationId,
                @Parameter(description = "Unique identifier of the confirmed order", required = true, example = "123e4567-e89b-12d3-a456-426614174777")
                @RequestParam UUID orderId,
                @AuthenticationPrincipal Jwt jwt) {
                UUID performedById = UUID.fromString(jwt.getSubject());
                confirmReservationUseCase.execute(
                                new ConfirmReservationUseCase.ConfirmReservationCommand(
                                                reservationId, orderId, performedById));
                return ResponseEntity.ok().build();
        }
}
