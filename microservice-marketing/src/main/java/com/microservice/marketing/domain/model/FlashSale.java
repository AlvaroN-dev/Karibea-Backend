package com.microservice.marketing.domain.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class FlashSale {
    private UUID id;
    private String name;
    private String description;
    private String bannerUrl;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private List<FlashSaleProduct> products;

    public FlashSale() {
    }

    public FlashSale(UUID id, String name, String description, String bannerUrl, LocalDateTime startedAt,
            LocalDateTime endedAt, Boolean isActive, LocalDateTime createdAt, List<FlashSaleProduct> products) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.bannerUrl = bannerUrl;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.products = products;
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

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
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

    public List<FlashSaleProduct> getProducts() {
        return products;
    }

    public void setProducts(List<FlashSaleProduct> products) {
        this.products = products;
    }
}
