package com.microservice.shopcart.application.usecases;

import com.microservice.shopcart.domain.exceptions.CartNotFoundException;
import com.microservice.shopcart.domain.models.ShoppingCart;
import com.microservice.shopcart.domain.port.in.ExpireCartPort;
import com.microservice.shopcart.domain.port.out.CartRepositoryPort;
import com.microservice.shopcart.domain.port.out.EventPublisherPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Use case implementation for expiring inactive shopping carts.
 */
@Service
public class ExpireCartUseCase implements ExpireCartPort {

    private static final Logger log = LoggerFactory.getLogger(ExpireCartUseCase.class);

    private final CartRepositoryPort cartRepository;
    private final EventPublisherPort eventPublisher;

    public ExpireCartUseCase(CartRepositoryPort cartRepository,
                             EventPublisherPort eventPublisher) {
        this.cartRepository = cartRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public void execute(UUID cartId) {
        ShoppingCart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new CartNotFoundException(cartId));

        cart.expire();
        cartRepository.save(cart);

        cart.getDomainEvents().forEach(eventPublisher::publish);
        cart.clearDomainEvents();

        log.info("Cart {} has been expired", cartId);
    }

    @Override
    @Transactional
    public int expireInactiveCarts() {
        List<ShoppingCart> expiredCarts = cartRepository.findExpiredCarts();
        
        int count = 0;
        for (ShoppingCart cart : expiredCarts) {
            try {
                cart.expire();
                cartRepository.save(cart);
                cart.getDomainEvents().forEach(eventPublisher::publish);
                cart.clearDomainEvents();
                count++;
            } catch (Exception e) {
                log.error("Failed to expire cart {}: {}", cart.getId(), e.getMessage());
            }
        }

        log.info("Expired {} inactive carts", count);
        return count;
    }
}
