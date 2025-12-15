package com.microservice.shopcart.domain.port.out;

import java.math.BigDecimal;

/**
 * Output port for validating coupons from Marketing microservice.
 */
public interface CouponServicePort {
    
    /**
     * Validates a coupon code and returns discount information.
     *
     * @param couponCode The coupon code
     * @param cartSubtotal The cart subtotal for percentage calculations
     * @param currency The cart currency
     * @return Coupon validation result
     */
    CouponValidationResult validateCoupon(String couponCode, BigDecimal cartSubtotal, String currency);
    
    /**
     * Coupon validation result from external service.
     */
    record CouponValidationResult(
        boolean isValid,
        String couponCode,
        String discountType,  // PERCENTAGE, FIXED_AMOUNT
        BigDecimal discountValue,
        BigDecimal discountAmount,
        String errorMessage
    ) {
        public static CouponValidationResult invalid(String errorMessage) {
            return new CouponValidationResult(false, null, null, null, null, errorMessage);
        }
    }
}
