package com.microservice.store.application.dto;

import java.util.UUID;

public class StoreResponseDto {

    private UUID id;
    private UUID externalOwnerUserId;
    private String name;
    private String description;
    private String email;
    private String phone;
    private String logoUrl;
    private String bannerUrl;
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

    private StoreStatusDto status;
    private java.util.List<StoreAddressDto> addresses;
    private StoreSettingsDto settings;

    public StoreStatusDto getStatus() {
        return status;
    }

    public void setStatus(StoreStatusDto status) {
        this.status = status;
    }

    public java.util.List<StoreAddressDto> getAddresses() {
        return addresses;
    }

    public void setAddresses(java.util.List<StoreAddressDto> addresses) {
        this.addresses = addresses;
    }

    public StoreSettingsDto getSettings() {
        return settings;
    }

    public void setSettings(StoreSettingsDto settings) {
        this.settings = settings;
    }

    // getters / setters
    
}
