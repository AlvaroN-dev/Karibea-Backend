package com.microservice.store.domain.models;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class Store {

    private UUID id;
    private UUID externalOwnerUserId;
    private String name;
    private String description;
    private String email;
    private String phone;
    private String logoUrl;
    private String bannerUrl;
    private StoreStatus status;
    private Double ratingAverage;
    private Integer totalReviews;
    private OffsetDateTime registeredAt;
    private boolean deleted;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime deletedAt;

    private List<StoreAddress> addresses;
    private StoreSettings settings;

    // getters / setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getExternalOwnerUserId() {
        return externalOwnerUserId;
    }

    public void setExternalOwnerUserId(UUID externalOwnerUserId) {
        this.externalOwnerUserId = externalOwnerUserId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public StoreStatus getStatus() {
        return status;
    }

    public void setStatus(StoreStatus status) {
        this.status = status;
    }

    public Double getRatingAverage() {
        return ratingAverage;
    }

    public void setRatingAverage(Double ratingAverage) {
        this.ratingAverage = ratingAverage;
    }

    public Integer getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(Integer totalReviews) {
        this.totalReviews = totalReviews;
    }

    public OffsetDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(OffsetDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public OffsetDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(OffsetDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public List<StoreAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<StoreAddress> addresses) {
        this.addresses = addresses;
    }

    public StoreSettings getSettings() {
        return settings;
    }

    public void setSettings(StoreSettings settings) {
        this.settings = settings;
    }

}
