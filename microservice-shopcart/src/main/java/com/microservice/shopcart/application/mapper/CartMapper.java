package com.microservice.shopcart.application.mapper;

import com.microservice.shopcart.application.dto.response.CartResponse;
import com.microservice.shopcart.application.dto.response.CouponResponse;
import com.microservice.shopcart.application.dto.response.ItemResponse;
import com.microservice.shopcart.domain.models.CouponApplied;
import com.microservice.shopcart.domain.models.Item;
import com.microservice.shopcart.domain.models.ShoppingCart;
import com.microservice.shopcart.domain.port.out.StoreServicePort;
import com.microservice.shopcart.domain.port.out.UserProfileServicePort;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Mapper for converting between domain models and DTOs.
 */
@Component
public class CartMapper {

    private final UserProfileServicePort userProfileService;
    private final StoreServicePort storeService;

    public CartMapper(UserProfileServicePort userProfileService,
                     StoreServicePort storeService) {
        this.userProfileService = userProfileService;
        this.storeService = storeService;
    }

    /**
     * Converts a ShoppingCart domain model to CartResponse DTO with enriched data.
     */
    public CartResponse toResponse(ShoppingCart cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setStatus(cart.getStatus().getName());
        response.setStatusDescription(cart.getStatus().getDescription());
        response.setCurrency(cart.getCurrency());
        response.setSubtotal(cart.getSubtotal().getAmount());
        response.setTotal(cart.getTotal().getAmount());
        response.setItemCount(cart.getItemCount());
        response.setNotes(cart.getNotes());
        response.setCreatedAt(cart.getCreatedAt());
        response.setUpdatedAt(cart.getUpdatedAt());
        response.setExpiresAt(cart.getExpiresAt());

        // Calculate total discount
        BigDecimal totalDiscount = cart.getCoupons().stream()
            .map(c -> c.getDiscountAmount().getAmount())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        response.setTotalDiscount(totalDiscount);

        // Enrich with user profile if available
        if (cart.getExternalUserProfileId() != null) {
            try {
                var userProfile = userProfileService.getUserProfile(cart.getExternalUserProfileId());
                if (userProfile != null) {
                    response.setUserProfile(new CartResponse.UserProfileInfo(
                        userProfile.userId(),
                        userProfile.firstName(),
                        userProfile.lastName(),
                        userProfile.email(),
                        userProfile.phone()
                    ));
                }
            } catch (Exception e) {
                // User profile service unavailable, continue without enrichment
            }
        }

        // Map items with enriched data
        response.setItems(cart.getItems().stream()
            .map(this::toItemResponse)
            .toList());

        // Map coupons
        response.setCoupons(cart.getCoupons().stream()
            .map(this::toCouponResponse)
            .toList());

        return response;
    }

    /**
     * Converts Item domain model to ItemResponse DTO with enriched product/store data.
     */
    public ItemResponse toItemResponse(Item item) {
        ItemResponse response = new ItemResponse();
        response.setId(item.getId());
        response.setQuantity(item.getQuantity().getValue());
        response.setUnitPrice(item.getUnitPrice().getAmount());
        response.setLineTotal(item.getLineTotal().getAmount());
        response.setAddedAt(item.getCreatedAt());
        response.setUpdatedAt(item.getUpdatedAt());

        // Product info (use stored denormalized data)
        ItemResponse.ProductInfo productInfo = new ItemResponse.ProductInfo();
        productInfo.setProductId(item.getExternalProductId());
        productInfo.setVariantId(item.getExternalVariantId());
        productInfo.setName(item.getProductName());
        productInfo.setVariantName(item.getVariantName());
        productInfo.setSku(item.getSku());
        productInfo.setImageUrl(item.getImageUrl());
        response.setProduct(productInfo);

        // Store info (fetch from service if available)
        if (item.getExternalStoreId() != null) {
            try {
                var storeInfo = storeService.getStore(item.getExternalStoreId());
                if (storeInfo != null) {
                    response.setStore(new ItemResponse.StoreInfo(
                        storeInfo.storeId(),
                        storeInfo.name(),
                        storeInfo.logoUrl()
                    ));
                }
            } catch (Exception e) {
                // Store service unavailable, create basic info
                ItemResponse.StoreInfo basicStore = new ItemResponse.StoreInfo();
                basicStore.setStoreId(item.getExternalStoreId());
                response.setStore(basicStore);
            }
        }

        return response;
    }

    /**
     * Converts CouponApplied domain model to CouponResponse DTO.
     */
    public CouponResponse toCouponResponse(CouponApplied coupon) {
        return new CouponResponse(
            coupon.getId(),
            coupon.getCode(),
            coupon.getDiscountType(),
            coupon.getDiscountAmount().getAmount(),
            coupon.getAppliedAt()
        );
    }

    /**
     * Converts list of carts to list of responses.
     */
    public List<CartResponse> toResponseList(List<ShoppingCart> carts) {
        return carts.stream()
            .map(this::toResponse)
            .toList();
    }
}
