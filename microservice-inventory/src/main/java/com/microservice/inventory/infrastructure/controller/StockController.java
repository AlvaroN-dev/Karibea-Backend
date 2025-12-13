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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/stock")
@Validated
@Tag(name = "Stock Management", description = "Endpoints for managing inventory, stock levels, and reservations")
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
        @Operation(summary = "Initialize Stock", description = "Creates a new stock entry for a product variant in a warehouse.")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Stock created successfully", content = @Content(schema = @Schema(implementation = StockResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized"),
                        @ApiResponse(responseCode = "403", description = "Forbidden")
        })
        public ResponseEntity<StockResponse> createStock(@Valid @RequestBody CreateStockRequest request) {
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
        @Operation(summary = "Get Stock Details", description = "Retrieves details of a specific stock record by its ID.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Stock found", content = @Content(schema = @Schema(implementation = StockResponse.class))),
                        @ApiResponse(responseCode = "404", description = "Stock not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        public ResponseEntity<StockResponse> getStock(
                        @Parameter(description = "ID of the stock") @PathVariable UUID stockId) {
                Stock stock = getStockAvailabilityUseCase.getById(stockId);
                return ResponseEntity.ok(stockMapper.toResponse(stock));
        }

        @GetMapping("/variant/{variantId}")
        @PreAuthorize("hasAuthority('SCOPE_inventory:read')")
        @Operation(summary = "Get Stock by Variant", description = "Retrieves all stock records for a given product variant across all warehouses.")
        public ResponseEntity<List<StockResponse>> getStockByVariant(
                        @Parameter(description = "ID of the product variant") @PathVariable UUID variantId) {
                List<Stock> stocks = getStockAvailabilityUseCase.getByVariant(variantId);
                return ResponseEntity.ok(stocks.stream().map(stockMapper::toResponse).toList());
        }

        @GetMapping("/warehouse/{warehouseId}")
        @PreAuthorize("hasAuthority('SCOPE_inventory:read')")
        @Operation(summary = "Get Stock by Warehouse", description = "Retrieves all stock records in a specific warehouse.")
        public ResponseEntity<List<StockResponse>> getStockByWarehouse(
                        @Parameter(description = "ID of the warehouse") @PathVariable UUID warehouseId) {
                List<Stock> stocks = getStockAvailabilityUseCase.getByWarehouse(warehouseId);
                return ResponseEntity.ok(stocks.stream().map(stockMapper::toResponse).toList());
        }

        @GetMapping("/warehouse/{warehouseId}/low-stock")
        @PreAuthorize("hasAuthority('SCOPE_inventory:read')")
        @Operation(summary = "Get Low Stock Items", description = "Retrieves items that are below their low stock threshold in a specific warehouse.")
        public ResponseEntity<List<StockResponse>> getLowStock(
                        @Parameter(description = "ID of the warehouse") @PathVariable UUID warehouseId) {
                List<Stock> stocks = getStockAvailabilityUseCase.getLowStockItems(warehouseId);
                return ResponseEntity.ok(stocks.stream().map(stockMapper::toResponse).toList());
        }

        @PostMapping("/adjust")
        @PreAuthorize("hasAuthority('SCOPE_inventory:write')")
        @Operation(summary = "Adjust Stock", description = "Manually increases or decreases stock quantity.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Stock adjusted successfully", content = @Content(schema = @Schema(implementation = StockResponse.class))),
                        @ApiResponse(responseCode = "404", description = "Stock not found"),
                        @ApiResponse(responseCode = "422", description = "Insufficient stock for decrease")
        })
        public ResponseEntity<StockResponse> adjustStock(
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
        @Operation(summary = "Reserve Stock", description = "Reserves stock for an order or cart.")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Stock reserved successfully", content = @Content(schema = @Schema(implementation = ReservationResponse.class))),
                        @ApiResponse(responseCode = "422", description = "Insufficient stock")
        })
        public ResponseEntity<ReservationResponse> reserveStock(@Valid @RequestBody ReserveStockRequest request) {
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
        @Operation(summary = "Release Reservation", description = "Releases a stock reservation, making the quantity available again.")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Reservation released"),
                        @ApiResponse(responseCode = "404", description = "Reservation not found")
        })
        public ResponseEntity<Void> releaseReservation(
                        @Parameter(description = "ID of the reservation") @PathVariable UUID reservationId,
                        @Parameter(description = "Reason for release") @RequestParam(required = false) String reason) {
                releaseReservationUseCase.execute(
                                new ReleaseReservationUseCase.ReleaseReservationCommand(reservationId, reason));
                return ResponseEntity.noContent().build();
        }

        @PostMapping("/reservations/{reservationId}/confirm")
        @PreAuthorize("hasAuthority('SCOPE_inventory:write')")
        @Operation(summary = "Confirm Reservation", description = "Confirms a reservation (e.g., after payment success).")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Reservation confirmed"),
                        @ApiResponse(responseCode = "404", description = "Reservation not found")
        })
        public ResponseEntity<Void> confirmReservation(
                        @Parameter(description = "ID of the reservation") @PathVariable UUID reservationId,
                        @Parameter(description = "ID of the confirmed order") @RequestParam UUID orderId,
                        @AuthenticationPrincipal Jwt jwt) {
                UUID performedById = UUID.fromString(jwt.getSubject());
                confirmReservationUseCase.execute(
                                new ConfirmReservationUseCase.ConfirmReservationCommand(
                                                reservationId, orderId, performedById));
                return ResponseEntity.ok().build();
        }
}
