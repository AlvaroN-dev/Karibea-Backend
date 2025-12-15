package com.microservice.store.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public class StoreCreateRequestDto {

    @NotBlank
    private UUID externalOwnerUserId;

    @NotBlank
    private String name;

    private String description;

    @Email
    private String email;

    private String phone;
    private String logoUrl;
    private String bannerUrl;
    // getters / setters
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

}
