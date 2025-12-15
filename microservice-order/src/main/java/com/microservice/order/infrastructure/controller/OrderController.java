package com.microservice.order.infrastructure.controller;

import com.microservice.order.application.dto.request.CancelOrderRequest;
import com.microservice.order.application.dto.request.ChangeOrderStatusRequest;
import com.microservice.order.application.dto.request.CreateOrderRequest;
import com.microservice.order.application.dto.response.OrderResponse;
import com.microservice.order.application.exception.OrderNotFoundException;
import com.microservice.order.application.mapper.OrderMapper;
import com.microservice.order.domain.models.Order;
import com.microservice.order.domain.port.in.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
 * REST Controller for Order operations.
 * 
 * Provides endpoints for creating, retrieving, and managing orders.
 * All endpoints require JWT authentication via OAuth2 Resource Server.
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Orders", description = "Order management API")
@SecurityRequirement(name = "bearer-jwt")
public class OrderController {

    private final CreateOrderPort createOrderPort;
    private final GetOrderPort getOrderPort;
    private final ConfirmOrderPort confirmOrderPort;
    private final CancelOrderPort cancelOrderPort;
    private final ChangeOrderStatusPort changeOrderStatusPort;
    private final OrderMapper orderMapper;

    /**
     * Create a new order.
     */
    @Operation(
            summary = "Create a new order",
            description = "Creates a new order with the specified items, addresses, and customer information"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Order created successfully",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "422", description = "Business rule violation")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            HttpServletRequest httpRequest,
            @AuthenticationPrincipal Jwt jwt) {

        log.info("Creating order for customer: {}", request.customerId());

        String ipAddress = httpRequest.getRemoteAddr();
        String userAgent = httpRequest.getHeader("User-Agent");

        CreateOrderPort.CreateOrderCommand command = orderMapper.toCommand(request, ipAddress, userAgent);
        Order order = createOrderPort.execute(command);

        return orderMapper.toResponse(order);
    }

    /**
     * Get order by ID.
     */
    @Operation(
            summary = "Get order by ID",
            description = "Retrieves a specific order by its unique identifier"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Order found",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{orderId}")
    public OrderResponse getOrderById(
            @Parameter(description = "Order unique identifier", required = true)
            @PathVariable UUID orderId) {
        log.debug("Getting order: {}", orderId);

        Order order = getOrderPort.findById(orderId)
                .orElseThrow(() -> OrderNotFoundException.withId(orderId));

        return orderMapper.toResponse(order);
    }

    /**
     * Get order by order number.
     */
    @Operation(
            summary = "Get order by order number",
            description = "Retrieves a specific order by its human-readable order number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Order found",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/number/{orderNumber}")
    public OrderResponse getOrderByNumber(
            @Parameter(description = "Human-readable order number", example = "ORD-20241215-A1B2C3D4")
            @PathVariable String orderNumber) {
        log.debug("Getting order by number: {}", orderNumber);

        Order order = getOrderPort.findByOrderNumber(orderNumber)
                .orElseThrow(() -> OrderNotFoundException.withOrderNumber(orderNumber));

        return orderMapper.toResponse(order);
    }

    /**
     * Get orders by customer ID.
     */
    @Operation(
            summary = "Get orders by customer",
            description = "Retrieves paginated orders for a specific customer"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/customer/{customerId}")
    public Page<OrderResponse> getOrdersByCustomer(
            @Parameter(description = "Customer unique identifier")
            @PathVariable UUID customerId,
            @PageableDefault(size = 10) Pageable pageable) {

        log.debug("Getting orders for customer: {}", customerId);

        return getOrderPort.findByCustomerId(customerId, pageable)
                .map(orderMapper::toResponse);
    }

    /**
     * Get orders by store ID.
     */
    @Operation(
            summary = "Get orders by store",
            description = "Retrieves paginated orders for a specific store"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/store/{storeId}")
    public Page<OrderResponse> getOrdersByStore(
            @Parameter(description = "Store unique identifier")
            @PathVariable UUID storeId,
            @PageableDefault(size = 10) Pageable pageable) {

        log.debug("Getting orders for store: {}", storeId);

        return getOrderPort.findByStoreId(storeId, pageable)
                .map(orderMapper::toResponse);
    }

    /**
     * Cancel an order.
     */
    @Operation(
            summary = "Cancel an order",
            description = "Cancels an order if it's in a cancellable state (PENDING, CONFIRMED, or PROCESSING)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Order cancelled successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "409", description = "Order cannot be cancelled in current state")
    })
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(
            @Parameter(description = "Order unique identifier")
            @PathVariable UUID orderId,
            @Valid @RequestBody CancelOrderRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        log.info("Cancelling order: {}", orderId);

        String cancelledBy = jwt != null ? jwt.getSubject() : "system";

        cancelOrderPort.execute(new CancelOrderPort.CancelOrderCommand(
                orderId,
                request.reason(),
                cancelledBy));

        return ResponseEntity.noContent().build();
    }

    /**
     * Confirm an order (internal/admin use).
     */
    @Operation(
            summary = "Confirm an order",
            description = "Confirms an order after payment verification. Typically called by payment service or admin."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Order confirmed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "409", description = "Invalid state transition")
    })
    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<Void> confirmOrder(
            @Parameter(description = "Order unique identifier")
            @PathVariable UUID orderId,
            @Parameter(description = "Payment transaction identifier")
            @RequestParam UUID paymentId,
            @AuthenticationPrincipal Jwt jwt) {

        log.info("Confirming order: {} with payment: {}", orderId, paymentId);

        String confirmedBy = jwt != null ? jwt.getSubject() : "system";

        confirmOrderPort.execute(new ConfirmOrderPort.ConfirmOrderCommand(
                orderId,
                paymentId,
                confirmedBy));

        return ResponseEntity.noContent().build();
    }

    /**
     * Change order status (internal/admin use).
     */
    @Operation(
            summary = "Change order status",
            description = "Manually changes the order status. For admin use or internal service calls."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Status changed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "409", description = "Invalid state transition")
    })
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Void> changeStatus(
            @Parameter(description = "Order unique identifier")
            @PathVariable UUID orderId,
            @Valid @RequestBody ChangeOrderStatusRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        log.info("Changing order {} status to: {}", orderId, request.newStatus());

        String changedBy = jwt != null ? jwt.getSubject() : "system";

        changeOrderStatusPort.execute(new ChangeOrderStatusPort.ChangeOrderStatusCommand(
                orderId,
                request.newStatus(),
                request.reason(),
                changedBy,
                request.relatedEntityId()));

        return ResponseEntity.noContent().build();
    }
}
