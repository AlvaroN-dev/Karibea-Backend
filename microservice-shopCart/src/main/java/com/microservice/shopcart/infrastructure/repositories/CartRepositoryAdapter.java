package com.microservice.shopcart.infrastructure.repositories;

import com.microservice.shopcart.domain.models.*;
import com.microservice.shopcart.domain.models.enums.CartStatus;
import com.microservice.shopcart.domain.port.out.CartRepositoryPort;
import com.microservice.shopcart.infrastructure.entities.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter implementing CartRepositoryPort using JPA.
 */
@Component
public class CartRepositoryAdapter implements CartRepositoryPort {

    private final JpaShoppingCartRepository cartRepository;
    private final JpaStatusRepository statusRepository;

    public CartRepositoryAdapter(JpaShoppingCartRepository cartRepository,
                                  JpaStatusRepository statusRepository) {
        this.cartRepository = cartRepository;
        this.statusRepository = statusRepository;
    }

    @Override
    @Transactional
    public ShoppingCart save(ShoppingCart cart) {
        ShoppingCartEntity entity = toEntity(cart);
        ShoppingCartEntity saved = cartRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShoppingCart> findById(UUID id) {
        return cartRepository.findByIdWithDetails(id)
            .map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShoppingCart> findActiveByUserProfileId(UUID userProfileId) {
        return cartRepository.findActiveByUserProfileId(userProfileId)
            .map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShoppingCart> findActiveBySessionId(String sessionId) {
        return cartRepository.findActiveBySessionId(sessionId)
            .map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShoppingCart> findExpiredCarts() {
        return cartRepository.findExpiredCarts(Instant.now())
            .stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void softDelete(UUID id) {
        cartRepository.findById(id).ifPresent(entity -> {
            entity.setIsDeleted(true);
            entity.setDeletedAt(Instant.now());
            cartRepository.save(entity);
        });
    }

    @Override
    public boolean existsById(UUID id) {
        return cartRepository.existsByIdAndNotDeleted(id);
    }

    // ============ Mapping Methods ============

    private ShoppingCartEntity toEntity(ShoppingCart cart) {
        ShoppingCartEntity entity = new ShoppingCartEntity();
        entity.setId(cart.getId());
        entity.setExternalUserProfilesId(cart.getExternalUserProfileId());
        entity.setSessionId(cart.getSessionId());
        entity.setCurrency(cart.getCurrency());
        entity.setSubtotal(cart.getSubtotal().getAmount());
        entity.setTotal(cart.getTotal().getAmount());
        entity.setItemCount(cart.getItemCount());
        entity.setNotes(cart.getNotes());
        entity.setCreatedAt(cart.getCreatedAt());
        entity.setUpdatedAt(cart.getUpdatedAt());
        entity.setExpiredAt(cart.getExpiresAt());
        entity.setDeletedAt(cart.getDeletedAt());
        entity.setIsDeleted(cart.isDeleted());

        // Set status
        if (cart.getStatusId() != null) {
            StatusEntity status = statusRepository.findById(cart.getStatusId())
                .orElseGet(() -> statusRepository.findByName(cart.getStatus().getName())
                    .orElse(null));
            entity.setStatus(status);
        }

        // Map items
        cart.getItems().forEach(item -> {
            ItemEntity itemEntity = toItemEntity(item);
            entity.addItem(itemEntity);
        });

        // Map coupons
        cart.getCoupons().forEach(coupon -> {
            CouponAppliedEntity couponEntity = toCouponEntity(coupon);
            entity.addCoupon(couponEntity);
        });

        return entity;
    }

    private ItemEntity toItemEntity(Item item) {
        ItemEntity entity = new ItemEntity();
        entity.setId(item.getId());
        entity.setExternalProductId(item.getExternalProductId());
        entity.setExternalVariantId(item.getExternalVariantId());
        entity.setExternalStoreId(item.getExternalStoreId());
        entity.setProductName(item.getProductName());
        entity.setVariantName(item.getVariantName());
        entity.setSku(item.getSku());
        entity.setImageUrl(item.getImageUrl());
        entity.setUnitPrice(item.getUnitPrice().getAmount());
        entity.setQuantity(item.getQuantity().getValue());
        entity.setLineTotal(item.getLineTotal().getAmount());
        entity.setExternalInventoryReservationId(item.getExternalInventoryReservationId());
        entity.setCreatedAt(item.getCreatedAt());
        entity.setUpdatedAt(item.getUpdatedAt());
        return entity;
    }

    private CouponAppliedEntity toCouponEntity(CouponApplied coupon) {
        CouponAppliedEntity entity = new CouponAppliedEntity();
        entity.setId(coupon.getId());
        entity.setCode(coupon.getCode());
        entity.setDiscountAmount(coupon.getDiscountAmount().getAmount());
        entity.setDiscountType(coupon.getDiscountType());
        entity.setCreatedAt(coupon.getCreatedAt());
        entity.setUpdatedAt(coupon.getUpdatedAt());
        entity.setAppliedAt(coupon.getAppliedAt());
        return entity;
    }

    private ShoppingCart toDomain(ShoppingCartEntity entity) {
        String currency = entity.getCurrency() != null ? entity.getCurrency() : "USD";
        
        // Map items
        List<Item> items = entity.getItems().stream()
            .map(itemEntity -> toItemDomain(itemEntity, currency))
            .collect(Collectors.toList());

        // Map coupons
        List<CouponApplied> coupons = entity.getCoupons().stream()
            .map(couponEntity -> toCouponDomain(couponEntity, currency))
            .collect(Collectors.toList());

        // Determine cart status
        CartStatus status = CartStatus.ACTIVE;
        if (entity.getStatus() != null && entity.getStatus().getName() != null) {
            try {
                status = CartStatus.fromName(entity.getStatus().getName());
            } catch (IllegalArgumentException e) {
                status = CartStatus.ACTIVE;
            }
        }

        return ShoppingCart.builder()
            .id(entity.getId())
            .externalUserProfileId(entity.getExternalUserProfilesId())
            .sessionId(entity.getSessionId())
            .statusId(entity.getStatus() != null ? entity.getStatus().getId() : 1L)
            .status(status)
            .currency(currency)
            .subtotal(Money.of(entity.getSubtotal() != null ? entity.getSubtotal() : java.math.BigDecimal.ZERO, currency))
            .total(Money.of(entity.getTotal() != null ? entity.getTotal() : java.math.BigDecimal.ZERO, currency))
            .itemCount(entity.getItemCount() != null ? entity.getItemCount() : 0)
            .notes(entity.getNotes())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .expiresAt(entity.getExpiredAt())
            .deletedAt(entity.getDeletedAt())
            .isDeleted(entity.getIsDeleted() != null && entity.getIsDeleted())
            .items(items)
            .coupons(coupons)
            .build();
    }

    private Item toItemDomain(ItemEntity entity, String currency) {
        return Item.builder()
            .id(entity.getId())
            .externalProductId(entity.getExternalProductId())
            .externalVariantId(entity.getExternalVariantId())
            .externalStoreId(entity.getExternalStoreId())
            .productName(entity.getProductName())
            .variantName(entity.getVariantName())
            .sku(entity.getSku())
            .imageUrl(entity.getImageUrl())
            .unitPrice(Money.of(entity.getUnitPrice(), currency))
            .quantity(Quantity.of(entity.getQuantity()))
            .lineTotal(Money.of(entity.getLineTotal(), currency))
            .externalInventoryReservationId(entity.getExternalInventoryReservationId())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    private CouponApplied toCouponDomain(CouponAppliedEntity entity, String currency) {
        return CouponApplied.builder()
            .id(entity.getId())
            .code(entity.getCode())
            .discountAmount(Money.of(entity.getDiscountAmount(), currency))
            .discountType(entity.getDiscountType())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .appliedAt(entity.getAppliedAt())
            .build();
    }
}
