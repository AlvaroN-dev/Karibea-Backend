package com.microservice.shopcart.application.usecases;

import com.microservice.shopcart.domain.exceptions.CartNotFoundException;
import com.microservice.shopcart.domain.models.ShoppingCart;
import com.microservice.shopcart.domain.port.in.RemoveItemPort;
import com.microservice.shopcart.domain.port.out.CartRepositoryPort;
import com.microservice.shopcart.domain.port.out.EventPublisherPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use case implementation for removing an item from a shopping cart.
 */
@Service
public class RemoveItemFromCartUseCase implements RemoveItemPort {

    private final CartRepositoryPort cartRepository;
    private final EventPublisherPort eventPublisher;

    public RemoveItemFromCartUseCase(CartRepositoryPort cartRepository,
                                     EventPublisherPort eventPublisher) {
        this.cartRepository = cartRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public void execute(UUID cartId, UUID itemId) {
        ShoppingCart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new CartNotFoundException(cartId));

        cart.removeItem(itemId);
        cartRepository.save(cart);

        cart.getDomainEvents().forEach(eventPublisher::publish);
        cart.clearDomainEvents();
    }
}
