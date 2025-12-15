package com.microservice.shopcart.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for shopping cart details with enriched information.
 */
@Schema(description = "Shopping cart response with full details")
public class CartResponse {

    @Schema(description = "Unique cart identifier", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Cart status", example = "Active")
    private String status;

    @Schema(description = "Cart status description", example = "Cart is active and can receive items")
    private String statusDescription;

    @Schema(description = "Currency code", example = "USD")
    private String currency;

    @Schema(description = "Cart subtotal before discounts", example = "150.00")
    private BigDecimal subtotal;

    @Schema(description = "Total discount amount", example = "15.00")
    private BigDecimal totalDiscount;

    @Schema(description = "Cart total after discounts", example = "135.00")
    private BigDecimal total;

    @Schema(description = "Total number of items in cart", example = "5")
    private Integer itemCount;

    @Schema(description = "Cart notes or special instructions", example = "Please gift wrap")
    private String notes;

    @Schema(description = "User profile information")
    private UserProfileInfo userProfile;

    @Schema(description = "List of items in the cart")
    private List<ItemResponse> items;

    @Schema(description = "List of applied coupons")
    private List<CouponResponse> coupons;

    @Schema(description = "Cart creation timestamp", example = "2024-12-13T10:30:00Z")
    private Instant createdAt;

    @Schema(description = "Last update timestamp", example = "2024-12-13T11:45:00Z")
    private Instant updatedAt;

    @Schema(description = "Cart expiration timestamp", example = "2024-12-20T10:30:00Z")
    private Instant expiresAt;

    // Constructors
    public CartResponse() {
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public UserProfileInfo getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfileInfo userProfile) {
        this.userProfile = userProfile;
    }

    public List<ItemResponse> getItems() {
        return items;
    }

    public void setItems(List<ItemResponse> items) {
        this.items = items;
    }

    public List<CouponResponse> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CouponResponse> coupons) {
        this.coupons = coupons;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    /**
     * Nested DTO for user profile information.
     */
    @Schema(description = "User profile information from User microservice")
    public static class UserProfileInfo {
        
        @Schema(description = "User profile ID", example = "550e8400-e29b-41d4-a716-446655440010")
        private UUID id;
        
        @Schema(description = "User's first name", example = "John")
        private String firstName;
        
        @Schema(description = "User's last name", example = "Doe")
        private String lastName;
        
        @Schema(description = "User's email", example = "john.doe@example.com")
        private String email;
        
        @Schema(description = "User's phone number", example = "+1234567890")
        private String phone;

        public UserProfileInfo() {
        }

        public UserProfileInfo(UUID id, String firstName, String lastName, String email, String phone) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.phone = phone;
        }

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
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
    }
}
