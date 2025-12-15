package com.microservice.marketing.infrastructure.adapter.mapper;

import com.microservice.marketing.application.dto.*;
import com.microservice.marketing.domain.model.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DtoMapper {

    public PromotionDTO toDto(Promotion domain) {
        if (domain == null) return null;
        return new PromotionDTO(
                domain.getId(),
                domain.getName(),
                domain.getDescription(),
                domain.getPromotionType(),
                domain.getDiscountValue(),
                domain.getMaxDiscountAmount(),
                domain.getMinPurchaseAmount(),
                domain.getAppliesTo(),
                domain.getExternalApplicableProductId(),
                domain.getExternalApplicableCategoryId(),
                domain.getExternalApplicableStoreId(),
                domain.getUsageLimit(),
                domain.getUsageCount(),
                domain.getPerUserLimit(),
                domain.getStartedAt(),
                domain.getEndedAt(),
                domain.getIsActive()
        );
    }

    public CouponDTO toDto(Coupon domain) {
        if (domain == null) return null;
        return new CouponDTO(
                domain.getId(),
                toDto(domain.getPromotion()),
                domain.getCode(),
                domain.getUsageLimit(),
                domain.getUsageCount(),
                domain.getPerUserLimit(),
                domain.getStartedAt(),
                domain.getEndedAt(),
                domain.getIsActive()
        );
    }

    public CouponUsageDTO toDto(CouponUsage domain) {
        if (domain == null) return null;
        return new CouponUsageDTO(
                domain.getId(),
                domain.getCouponId(),
                domain.getExternalUserProfileId(),
                domain.getExternalOrderId(),
                domain.getDiscountAmount(),
                domain.getUsedAt()
        );
    }

    public FlashSaleProductDTO toDto(FlashSaleProduct domain) {
        if (domain == null) return null;
        return new FlashSaleProductDTO(
                domain.getId(),
                domain.getFlashSaleId(),
                domain.getExternalProductId(),
                domain.getSalePrice(),
                domain.getQuantityLimit(),
                domain.getQuantitySold()
        );
    }

    public FlashSaleDTO toDto(FlashSale domain) {
        if (domain == null) return null;
        return new FlashSaleDTO(
                domain.getId(),
                domain.getName(),
                domain.getDescription(),
                domain.getBannerUrl(),
                domain.getStartedAt(),
                domain.getEndedAt(),
                domain.getIsActive(),
                domain.getProducts() != null ? domain.getProducts().stream()
                        .map(this::toDto)
                        .collect(Collectors.toList()) : null
        );
    }
}
