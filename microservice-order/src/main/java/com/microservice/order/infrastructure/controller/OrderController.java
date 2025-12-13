package com.microservice.order.infrastructure.controller;

import com.microservice.order.application.dto.request.CancelOrderRequest;
import com.microservice.order.application.dto.request.ChangeOrderStatusRequest;
import com.microservice.order.application.dto.request.CreateOrderRequest;
import com.microservice.order.application.dto.response.OrderResponse;
import com.microservice.order.application.exception.OrderNotFoundException;
import com.microservice.order.application.mapper.OrderMapper;
import com.microservice.order.domain.models.Order;
import com.microservice.order.domain.port.in.*;
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
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
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
    @GetMapping("/{orderId}")
    public OrderResponse getOrderById(@PathVariable UUID orderId) {
        log.debug("Getting order: {}", orderId);

        Order order = getOrderPort.findById(orderId)
                .orElseThrow(() -> OrderNotFoundException.withId(orderId));

        return orderMapper.toResponse(order);
    }

    /**
     * Get order by order number.
     */
    @GetMapping("/number/{orderNumber}")
    public OrderResponse getOrderByNumber(@PathVariable String orderNumber) {
        log.debug("Getting order by number: {}", orderNumber);

        Order order = getOrderPort.findByOrderNumber(orderNumber)
                .orElseThrow(() -> OrderNotFoundException.withOrderNumber(orderNumber));

        return orderMapper.toResponse(order);
    }

    /**
     * Get orders by customer ID.
     */
    @GetMapping("/customer/{customerId}")
    public Page<OrderResponse> getOrdersByCustomer(
            @PathVariable UUID customerId,
            @PageableDefault(size = 10) Pageable pageable) {

        log.debug("Getting orders for customer: {}", customerId);

        return getOrderPort.findByCustomerId(customerId, pageable)
                .map(orderMapper::toResponse);
    }

    /**
     * Get orders by store ID.
     */
    @GetMapping("/store/{storeId}")
    public Page<OrderResponse> getOrdersByStore(
            @PathVariable UUID storeId,
            @PageableDefault(size = 10) Pageable pageable) {

        log.debug("Getting orders for store: {}", storeId);

        return getOrderPort.findByStoreId(storeId, pageable)
                .map(orderMapper::toResponse);
    }

    /**
     * Cancel an order.
     */
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(
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
    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<Void> confirmOrder(
            @PathVariable UUID orderId,
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
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Void> changeStatus(
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
