package com.microservice.shopcart.infrastructure.adapters;

import com.microservice.shopcart.application.exception.ExternalServiceException;
import com.microservice.shopcart.domain.port.out.CouponServicePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Duration;

/**
 * Adapter for communicating with Marketing microservice via WebClient.
 */
@Component
public class CouponServiceAdapter implements CouponServicePort {

    private final WebClient webClient;
    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    public CouponServiceAdapter(WebClient.Builder webClientBuilder,
                                 @Value("${services.marketing.url:http://localhost:8082}") String marketingUrl) {
        this.webClient = webClientBuilder
            .baseUrl(marketingUrl)
            .build();
    }

    @Override
    public CouponValidationResult validateCoupon(String couponCode, BigDecimal cartSubtotal, String currency) {
        try {
            CouponValidationRequest request = new CouponValidationRequest(couponCode, cartSubtotal, currency);

            MarketingCouponResponse response = webClient.post()
                .uri("/api/v1/coupons/validate")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(MarketingCouponResponse.class)
                .timeout(TIMEOUT)
                .block();

            if (response == null) {
                return CouponValidationResult.invalid("Invalid coupon response");
            }

            if (!response.isValid()) {
                return CouponValidationResult.invalid(response.errorMessage());
            }

            return new CouponValidationResult(
                true,
                response.couponCode(),
                response.discountType(),
                response.discountValue(),
                response.discountAmount(),
                null
            );
        } catch (Exception e) {
            throw new ExternalServiceException("Marketing", "Failed to validate coupon: " + e.getMessage(), e);
        }
    }

    private record CouponValidationRequest(
        String couponCode,
        BigDecimal cartSubtotal,
        String currency
    ) {}

    private record MarketingCouponResponse(
        boolean isValid,
        String couponCode,
        String discountType,
        BigDecimal discountValue,
        BigDecimal discountAmount,
        String errorMessage
    ) {}
}
