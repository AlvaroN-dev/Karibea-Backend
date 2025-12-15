package com.microservice.shopcart.application.usecases;

import com.microservice.shopcart.domain.exceptions.CartNotFoundException;
import com.microservice.shopcart.domain.models.ShoppingCart;
import com.microservice.shopcart.domain.port.in.DeleteCartPort;
import com.microservice.shopcart.domain.port.out.CartRepositoryPort;
import com.microservice.shopcart.domain.port.out.EventPublisherPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use case implementation for soft deleting a shopping cart.
 */
@Service
public class DeleteCartUseCase implements DeleteCartPort {

    private static final Logger log = LoggerFactory.getLogger(DeleteCartUseCase.class);

    private final CartRepositoryPort cartRepository;
    private final EventPublisherPort eventPublisher;

    public DeleteCartUseCase(CartRepositoryPort cartRepository,
                             EventPublisherPort eventPublisher) {
        this.cartRepository = cartRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public void execute(UUID cartId) {
        ShoppingCart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new CartNotFoundException(cartId));

        // Apply soft delete in domain
        cart.softDelete();
        
        // Persist the soft delete
        cartRepository.save(cart);

        // Publish domain events
        cart.getDomainEvents().forEach(eventPublisher::publish);
        cart.clearDomainEvents();

        log.info("Cart {} has been soft deleted", cartId);
    }
}
