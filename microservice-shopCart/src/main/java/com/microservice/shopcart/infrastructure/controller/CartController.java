package com.microservice.shopcart.infrastructure.controller;

import com.microservice.shopcart.application.dto.request.*;
import com.microservice.shopcart.application.dto.response.*;
import com.microservice.shopcart.application.services.CartApplicationService;
import com.microservice.shopcart.infrastructure.exceptions.advice.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for Shopping Cart operations.
 */
@RestController
@RequestMapping("/api/v1/carts")
@Tag(name = "Shopping Cart", description = "Shopping Cart management API endpoints")
public class CartController {

    private final CartApplicationService cartService;

    public CartController(CartApplicationService cartService) {
        this.cartService = cartService;
    }

    // ==================== CART OPERATIONS ====================

    @Operation(
        summary = "Create a new shopping cart",
        description = "Creates a new shopping cart for a user or guest session. " +
                      "If a cart already exists for the user/session, returns the existing cart."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cart created successfully",
                     content = @Content(schema = @Schema(implementation = CartCreatedResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data",
                     content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<CartCreatedResponse> createCart(
            @Valid @RequestBody CreateCartRequest request) {
        CartCreatedResponse response = cartService.createCart(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
        summary = "Get shopping cart by ID",
        description = "Retrieves the shopping cart details including items, coupons, and enriched product/store information."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cart retrieved successfully",
                     content = @Content(schema = @Schema(implementation = CartResponse.class))),
        @ApiResponse(responseCode = "404", description = "Cart not found",
                     content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{cartId}")
    public ResponseEntity<CartResponse> getCart(
            @Parameter(description = "Cart UUID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID cartId) {
        return ResponseEntity.ok(cartService.getCart(cartId));
    }

    @Operation(
        summary = "Get shopping cart by user profile ID",
        description = "Retrieves the active shopping cart for a specific user."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cart retrieved successfully",
                     content = @Content(schema = @Schema(implementation = CartResponse.class))),
        @ApiResponse(responseCode = "404", description = "No active cart found for user",
                     content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/user/{userProfileId}")
    public ResponseEntity<CartResponse> getCartByUser(
            @Parameter(description = "User Profile UUID", required = true, example = "550e8400-e29b-41d4-a716-446655440010")
            @PathVariable UUID userProfileId) {
        return ResponseEntity.ok(cartService.getCartByUserProfile(userProfileId));
    }

    @Operation(
        summary = "Get shopping cart by session ID",
        description = "Retrieves the active shopping cart for a guest session."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cart retrieved successfully",
                     content = @Content(schema = @Schema(implementation = CartResponse.class))),
        @ApiResponse(responseCode = "404", description = "No active cart found for session",
                     content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<CartResponse> getCartBySession(
            @Parameter(description = "Session ID", required = true, example = "sess_abc123xyz789")
            @PathVariable String sessionId) {
        return ResponseEntity.ok(cartService.getCartBySession(sessionId));
    }

    @Operation(
        summary = "Clear all items from cart",
        description = "Removes all items from the shopping cart while keeping the cart active."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cart cleared successfully",
                     content = @Content(schema = @Schema(implementation = MessageResponse.class))),
        @ApiResponse(responseCode = "404", description = "Cart not found",
                     content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<MessageResponse> clearCart(
            @Parameter(description = "Cart UUID", required = true)
            @PathVariable UUID cartId) {
        return ResponseEntity.ok(cartService.clearCart(cartId));
    }

    @Operation(
        summary = "Delete shopping cart (soft delete)",
        description = "Performs a soft delete on the shopping cart. The cart is marked as deleted but remains in the database for audit purposes."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cart deleted successfully",
                     content = @Content(schema = @Schema(implementation = MessageResponse.class))),
        @ApiResponse(responseCode = "404", description = "Cart not found",
                     content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{cartId}")
    public ResponseEntity<MessageResponse> deleteCart(
            @Parameter(description = "Cart UUID", required = true)
            @PathVariable UUID cartId) {
        return ResponseEntity.ok(cartService.deleteCart(cartId));
    }

    // ==================== ITEM OPERATIONS ====================

    @Operation(
        summary = "Add item to cart",
        description = "Adds a product item to the shopping cart. If the same product/variant already exists, quantities are merged."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item added successfully",
                     content = @Content(schema = @Schema(implementation = CartResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data",
                     content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Cart not found",
                     content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "503", description = "External service unavailable",
                     content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartResponse> addItem(
            @Parameter(description = "Cart UUID", required = true)
            @PathVariable UUID cartId,
            @Valid @RequestBody AddItemRequest request) {
        return ResponseEntity.ok(cartService.addItem(cartId, request));
    }

    @Operation(
        summary = "Update item quantity",
        description = "Updates the quantity of an existing item in the cart."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item quantity updated successfully",
                     content = @Content(schema = @Schema(implementation = CartResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid quantity",
                     content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Cart or item not found",
                     content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<CartResponse> updateItemQuantity(
            @Parameter(description = "Cart UUID", required = true)
            @PathVariable UUID cartId,
            @Parameter(description = "Item UUID", required = true)
            @PathVariable UUID itemId,
            @Valid @RequestBody UpdateItemQuantityRequest request) {
        return ResponseEntity.ok(cartService.updateItemQuantity(cartId, itemId, request));
    }

    @Operation(
        summary = "Remove item from cart",
        description = "Removes an item from the shopping cart."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item removed successfully",
                     content = @Content(schema = @Schema(implementation = CartResponse.class))),
        @ApiResponse(responseCode = "404", description = "Cart or item not found",
                     content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<CartResponse> removeItem(
            @Parameter(description = "Cart UUID", required = true)
            @PathVariable UUID cartId,
            @Parameter(description = "Item UUID", required = true)
            @PathVariable UUID itemId) {
        return ResponseEntity.ok(cartService.removeItem(cartId, itemId));
    }

    // ==================== COUPON OPERATIONS ====================

    @Operation(
        summary = "Apply coupon to cart",
        description = "Applies a discount coupon to the shopping cart. The coupon is validated against the Marketing service."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Coupon applied successfully",
                     content = @Content(schema = @Schema(implementation = CartResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid coupon or already applied",
                     content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Cart not found",
                     content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "503", description = "Marketing service unavailable",
                     content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{cartId}/coupons")
    public ResponseEntity<CartResponse> applyCoupon(
            @Parameter(description = "Cart UUID", required = true)
            @PathVariable UUID cartId,
            @Valid @RequestBody ApplyCouponRequest request) {
        return ResponseEntity.ok(cartService.applyCoupon(cartId, request));
    }

    @Operation(
        summary = "Remove coupon from cart",
        description = "Removes an applied coupon from the shopping cart."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Coupon removed successfully",
                     content = @Content(schema = @Schema(implementation = CartResponse.class))),
        @ApiResponse(responseCode = "404", description = "Cart or coupon not found",
                     content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{cartId}/coupons/{couponId}")
    public ResponseEntity<CartResponse> removeCoupon(
            @Parameter(description = "Cart UUID", required = true)
            @PathVariable UUID cartId,
            @Parameter(description = "Coupon UUID", required = true)
            @PathVariable UUID couponId) {
        return ResponseEntity.ok(cartService.removeCoupon(cartId, couponId));
    }
}
