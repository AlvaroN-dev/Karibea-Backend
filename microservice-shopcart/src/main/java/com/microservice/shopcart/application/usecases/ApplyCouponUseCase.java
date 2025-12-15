package com.microservice.shopcart.application.usecases;

import com.microservice.shopcart.application.exception.ExternalServiceException;
import com.microservice.shopcart.domain.exceptions.CartNotFoundException;
import com.microservice.shopcart.domain.models.CouponApplied;
import com.microservice.shopcart.domain.models.Money;
import com.microservice.shopcart.domain.models.ShoppingCart;
import com.microservice.shopcart.domain.port.in.ApplyCouponPort;
import com.microservice.shopcart.domain.port.out.CartRepositoryPort;
import com.microservice.shopcart.domain.port.out.CouponServicePort;
import com.microservice.shopcart.domain.port.out.EventPublisherPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use case implementation for applying a coupon to a shopping cart.
 */
@Service
public class ApplyCouponUseCase implements ApplyCouponPort {

    private final CartRepositoryPort cartRepository;
    private final CouponServicePort couponService;
    private final EventPublisherPort eventPublisher;

    public ApplyCouponUseCase(CartRepositoryPort cartRepository,
                              CouponServicePort couponService,
                              EventPublisherPort eventPublisher) {
        this.cartRepository = cartRepository;
        this.couponService = couponService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public void execute(UUID cartId, String couponCode) {
        ShoppingCart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new CartNotFoundException(cartId));

        // Validate coupon with Marketing service
        CouponServicePort.CouponValidationResult validationResult;
        try {
            validationResult = couponService.validateCoupon(
                couponCode, 
                cart.getSubtotal().getAmount(), 
                cart.getCurrency()
            );
        } catch (Exception e) {
            throw new ExternalServiceException("Marketing", "Failed to validate coupon", e);
        }

        if (!validationResult.isValid()) {
            throw new ExternalServiceException("Marketing", validationResult.errorMessage());
        }

        // Create coupon applied entity
        CouponApplied coupon = CouponApplied.builder()
            .code(validationResult.couponCode())
            .discountType(validationResult.discountType())
            .discountAmount(Money.of(validationResult.discountAmount(), cart.getCurrency()))
            .build();

        // Apply to cart
        cart.applyCoupon(coupon);
        cartRepository.save(cart);

        cart.getDomainEvents().forEach(eventPublisher::publish);
        cart.clearDomainEvents();
    }
}
