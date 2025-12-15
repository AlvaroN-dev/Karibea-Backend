package com.microservice.shopcart.application.services;

import com.microservice.shopcart.application.dto.request.*;
import com.microservice.shopcart.application.dto.response.*;
import com.microservice.shopcart.application.mapper.CartMapper;
import com.microservice.shopcart.domain.exceptions.CartNotFoundException;
import com.microservice.shopcart.domain.models.ShoppingCart;
import com.microservice.shopcart.domain.port.in.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Application service that orchestrates cart operations.
 * Acts as a facade for the use cases.
 */
@Service
public class CartApplicationService {

    private final CreateCartPort createCartPort;
    private final GetCartPort getCartPort;
    private final AddItemPort addItemPort;
    private final UpdateItemQuantityPort updateItemQuantityPort;
    private final RemoveItemPort removeItemPort;
    private final ApplyCouponPort applyCouponPort;
    private final RemoveCouponPort removeCouponPort;
    private final ClearCartPort clearCartPort;
    private final DeleteCartPort deleteCartPort;
    private final CartMapper cartMapper;

    public CartApplicationService(
            CreateCartPort createCartPort,
            GetCartPort getCartPort,
            AddItemPort addItemPort,
            UpdateItemQuantityPort updateItemQuantityPort,
            RemoveItemPort removeItemPort,
            ApplyCouponPort applyCouponPort,
            RemoveCouponPort removeCouponPort,
            ClearCartPort clearCartPort,
            DeleteCartPort deleteCartPort,
            CartMapper cartMapper) {
        this.createCartPort = createCartPort;
        this.getCartPort = getCartPort;
        this.addItemPort = addItemPort;
        this.updateItemQuantityPort = updateItemQuantityPort;
        this.removeItemPort = removeItemPort;
        this.applyCouponPort = applyCouponPort;
        this.removeCouponPort = removeCouponPort;
        this.clearCartPort = clearCartPort;
        this.deleteCartPort = deleteCartPort;
        this.cartMapper = cartMapper;
    }

    /**
     * Creates a new shopping cart.
     */
    public CartCreatedResponse createCart(CreateCartRequest request) {
        ShoppingCart cart = createCartPort.execute(
            request.getUserProfileId(),
            request.getSessionId(),
            request.getCurrency()
        );
        return CartCreatedResponse.of(cart.getId());
    }

    /**
     * Gets a shopping cart by ID with enriched data.
     */
    public CartResponse getCart(UUID cartId) {
        ShoppingCart cart = getCartPort.execute(cartId)
            .orElseThrow(() -> new CartNotFoundException(cartId));
        return cartMapper.toResponse(cart);
    }

    /**
     * Gets a shopping cart by user profile ID.
     */
    public CartResponse getCartByUserProfile(UUID userProfileId) {
        ShoppingCart cart = getCartPort.findByUserProfileId(userProfileId)
            .orElseThrow(() -> new CartNotFoundException("No active cart found for user"));
        return cartMapper.toResponse(cart);
    }

    /**
     * Gets a shopping cart by session ID.
     */
    public CartResponse getCartBySession(String sessionId) {
        ShoppingCart cart = getCartPort.findBySessionId(sessionId)
            .orElseThrow(() -> new CartNotFoundException("No active cart found for session"));
        return cartMapper.toResponse(cart);
    }

    /**
     * Adds an item to the cart.
     */
    public CartResponse addItem(UUID cartId, AddItemRequest request) {
        addItemPort.execute(
            cartId,
            request.getProductId(),
            request.getVariantId(),
            request.getStoreId(),
            request.getQuantity()
        );
        return getCart(cartId);
    }

    /**
     * Updates item quantity.
     */
    public CartResponse updateItemQuantity(UUID cartId, UUID itemId, UpdateItemQuantityRequest request) {
        updateItemQuantityPort.execute(cartId, itemId, request.getQuantity());
        return getCart(cartId);
    }

    /**
     * Removes an item from the cart.
     */
    public CartResponse removeItem(UUID cartId, UUID itemId) {
        removeItemPort.execute(cartId, itemId);
        return getCart(cartId);
    }

    /**
     * Applies a coupon to the cart.
     */
    public CartResponse applyCoupon(UUID cartId, ApplyCouponRequest request) {
        applyCouponPort.execute(cartId, request.getCouponCode());
        return getCart(cartId);
    }

    /**
     * Removes a coupon from the cart.
     */
    public CartResponse removeCoupon(UUID cartId, UUID couponId) {
        removeCouponPort.execute(cartId, couponId);
        return getCart(cartId);
    }

    /**
     * Clears all items from the cart.
     */
    public MessageResponse clearCart(UUID cartId) {
        clearCartPort.execute(cartId);
        return MessageResponse.success("Cart cleared successfully");
    }

    /**
     * Deletes a cart (soft delete).
     */
    public MessageResponse deleteCart(UUID cartId) {
        deleteCartPort.execute(cartId);
        return MessageResponse.success("Cart deleted successfully");
    }
}
