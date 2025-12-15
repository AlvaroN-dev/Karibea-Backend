package com.microservice.marketing.domain.model;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.UUID;
import com.microservice.marketing.domain.model.Promotion;

public class Coupon {
    private UUID id;
    private UUID promotionId;
    private Promotion promotion;
    private String code;
    private Integer usageLimit;
    private Integer usageCount;
    private Integer perUserLimit;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Coupon() {
    }

    public Coupon(UUID id, UUID promotionId, Promotion promotion, String code, Integer usageLimit, Integer usageCount, Integer perUserLimit,
            LocalDateTime startedAt, LocalDateTime endedAt, Boolean isActive, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.promotionId = promotionId;
        this.promotion = promotion;
        this.code = code;
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

    public UUID getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(UUID promotionId) {
        this.promotionId = promotionId;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
