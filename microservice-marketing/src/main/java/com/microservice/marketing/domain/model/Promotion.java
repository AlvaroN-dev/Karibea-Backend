package com.microservice.marketing.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Promotion {
    private UUID id;
    private String name;
    private String description;
    private String promotionType;
    private BigDecimal discountValue;
    private BigDecimal maxDiscountAmount;
    private BigDecimal minPurchaseAmount;
    private String appliesTo;
    private String externalApplicableProductId;
    private String externalApplicableCategoryId;
    private String externalApplicableStoreId;
    private Integer usageLimit;
    private Integer usageCount;
    private Integer perUserLimit;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Promotion() {
    }

    public Promotion(UUID id, String name, String description, String promotionType, BigDecimal discountValue,
            BigDecimal maxDiscountAmount, BigDecimal minPurchaseAmount, String appliesTo,
            String externalApplicableProductId, String externalApplicableCategoryId,
            String externalApplicableStoreId, Integer usageLimit, Integer usageCount, Integer perUserLimit,
            LocalDateTime startedAt, LocalDateTime endedAt, Boolean isActive, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.promotionType = promotionType;
        this.discountValue = discountValue;
        this.maxDiscountAmount = maxDiscountAmount;
        this.minPurchaseAmount = minPurchaseAmount;
        this.appliesTo = appliesTo;
        this.externalApplicableProductId = externalApplicableProductId;
        this.externalApplicableCategoryId = externalApplicableCategoryId;
        this.externalApplicableStoreId = externalApplicableStoreId;
        this.usageLimit = usageLimit;
        this.usageCount = usageCount;
        this.perUserLimit = perUserLimit;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimal getMaxDiscountAmount() {
        return maxDiscountAmount;
    }

    public void setMaxDiscountAmount(BigDecimal maxDiscountAmount) {
        this.maxDiscountAmount = maxDiscountAmount;
    }

    public BigDecimal getMinPurchaseAmount() {
        return minPurchaseAmount;
    }

    public void setMinPurchaseAmount(BigDecimal minPurchaseAmount) {
        this.minPurchaseAmount = minPurchaseAmount;
    }

    public String getAppliesTo() {
        return appliesTo;
    }

    public void setAppliesTo(String appliesTo) {
        this.appliesTo = appliesTo;
    }

    public String getExternalApplicableProductId() {
        return externalApplicableProductId;
    }

    public void setExternalApplicableProductId(String externalApplicableProductId) {
        this.externalApplicableProductId = externalApplicableProductId;
    }

    public String getExternalApplicableCategoryId() {
        return externalApplicableCategoryId;
    }

    public void setExternalApplicableCategoryId(String externalApplicableCategoryId) {
        this.externalApplicableCategoryId = externalApplicableCategoryId;
    }

    public String getExternalApplicableStoreId() {
        return externalApplicableStoreId;
    }

    public void setExternalApplicableStoreId(String externalApplicableStoreId) {
        this.externalApplicableStoreId = externalApplicableStoreId;
    }

    public Integer getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(Integer usageLimit) {
        this.usageLimit = usageLimit;
    }

    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }

    public Integer getPerUserLimit() {
        return perUserLimit;
    }

    public void setPerUserLimit(Integer perUserLimit) {
        this.perUserLimit = perUserLimit;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
