package com.microservice.shoppingCart.domain.service;

import java.util.Optional;
import java.util.UUID;

import com.microservice.shoppingCart.domain.exception.CouponInvalidException;
import com.microservice.shoppingCart.domain.exception.ItemNotFoundException;
import com.microservice.shoppingCart.domain.exception.ShoppingCartNotFoundException;
import com.microservice.shoppingCart.domain.exception.StatusInvalidException;
import com.microservice.shoppingCart.domain.model.CouponApplied;
import com.microservice.shoppingCart.domain.model.Item;
import com.microservice.shoppingCart.domain.model.ShoppingCart;
import com.microservice.shoppingCart.domain.model.Status;
import com.microservice.shoppingCart.domain.port.out.CouponRepositoryPort;
import com.microservice.shoppingCart.domain.port.out.ItemRepositoryPort;
import com.microservice.shoppingCart.domain.port.out.ShoppingCartRepositoryPort;
import com.microservice.shoppingCart.domain.port.out.StatusRepositoryPort;

/**
 * Servicio de dominio que orquesta reglas entre entidades y repositorios (ports out).
 */
public class ShoppingCartService {

    private final ShoppingCartRepositoryPort cartRepo;
    private final ItemRepositoryPort itemRepo;
    private final CouponRepositoryPort couponRepo;
    private final StatusRepositoryPort statusRepo;

    public ShoppingCartService(ShoppingCartRepositoryPort cartRepo,
                            ItemRepositoryPort itemRepo,
                            CouponRepositoryPort couponRepo,
                            StatusRepositoryPort statusRepo) {
        this.cartRepo = cartRepo;
        this.itemRepo = itemRepo;
        this.couponRepo = couponRepo;
        this.statusRepo = statusRepo;
    }

    public ShoppingCart createCart(ShoppingCart cart) {
        // initial validations
        if (cart == null) {
            throw new IllegalArgumentException("Cart cannot be null");
        }
        cart.recalculateCart();
        return cartRepo.save(cart);
    }

    public ShoppingCart getCart(UUID cartId) {
        return cartRepo.findById(cartId)
                .orElseThrow(() -> new ShoppingCartNotFoundException("Cart not found: " + cartId));
    }

    public void addItem(UUID cartId, Item item) {
        ShoppingCart cart = getCart(cartId);
        cart.addItem(item);
        // persist item via itemRepo (adapter out) and update cart
        item.setShoppingCartId(cart.getId());
        itemRepo.save(item);
        cartRepo.save(cart);
    }

    public void removeItem(UUID cartId, UUID itemId) {
        ShoppingCart cart = getCart(cartId);
        boolean removed = cart.removeItemById(itemId);
        if (!removed) {
            throw new ItemNotFoundException("Item not found in cart: " + itemId);
        }
        itemRepo.deleteById(itemId);
        cartRepo.save(cart);
    }

    public void applyCoupon(UUID cartId, CouponApplied coupon) {
        ShoppingCart cart = getCart(cartId);
        // domain-level validation: check coupon uniqueness in repository or business rules
        Optional<CouponApplied> existing = couponRepo.findByCode(coupon.getCode());
        if (existing.isPresent()) {
            // If coupon repository already tracks global redemption rules, you can throw.
            // Here, allow local application but prevent duplicate codes in cart (cart.addCoupon already prevents duplicates).
            // We'll throw if coupon is globally invalid (example).
            // throw new CouponInvalidException("Coupon code already used globally: " + coupon.getCode());
        }
        try {
            cart.applyCoupon(coupon);
            coupon.setShoppingCartId(cart.getId());
            couponRepo.save(coupon);
            cartRepo.save(cart);
        } catch (IllegalArgumentException ex) {
            throw new CouponInvalidException(ex.getMessage());
        }
    }

    public void updateStatus(UUID cartId, Status newStatus) {
        ShoppingCart cart = getCart(cartId);
        if (newStatus.getId() != null) {
            // validate existence in status repository
            boolean exists = statusRepo.findById(newStatus.getId()).isPresent();
            if (!exists) {
                throw new StatusInvalidException("Status id not found: " + newStatus.getId());
            }
        } else if (newStatus.getName() != null) {
            boolean exists = statusRepo.findByName(newStatus.getName()).isPresent();
            if (!exists) {
                throw new StatusInvalidException("Status name not found: " + newStatus.getName());
            }
        } else {
            throw new StatusInvalidException("Status must contain id or name");
        }

        cart.updateStatus(newStatus);
        cartRepo.save(cart);
    }
}
