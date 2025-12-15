package com.microservice.shopcart.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for applying a coupon to a shopping cart.
 */
@Schema(description = "Request to apply a coupon to the shopping cart")
public class ApplyCouponRequest {

    @Schema(description = "Coupon code to apply", 
            example = "SUMMER2024",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 3,
            maxLength = 50)
    @NotBlank(message = "Coupon code is required")
    @Size(min = 3, max = 50, message = "Coupon code must be between 3 and 50 characters")
    private String couponCode;

    // Constructors
    public ApplyCouponRequest() {
    }

    public ApplyCouponRequest(String couponCode) {
        this.couponCode = couponCode;
    }

    // Getters and Setters
    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
}
