package com.microservice.shopcart.application.usecases;

import com.microservice.shopcart.domain.exceptions.CartNotFoundException;
import com.microservice.shopcart.domain.models.ShoppingCart;
import com.microservice.shopcart.domain.port.in.ClearCartPort;
import com.microservice.shopcart.domain.port.out.CartRepositoryPort;
import com.microservice.shopcart.domain.port.out.EventPublisherPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use case implementation for clearing all items from a shopping cart.
 */
@Service
public class ClearCartUseCase implements ClearCartPort {

    private final CartRepositoryPort cartRepository;
    private final EventPublisherPort eventPublisher;

    public ClearCartUseCase(CartRepositoryPort cartRepository,
                           EventPublisherPort eventPublisher) {
        this.cartRepository = cartRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public void execute(UUID cartId) {
        ShoppingCart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new CartNotFoundException(cartId));

        // Remove all items - get a copy of item IDs first
        cart.getItems().stream()
            .map(item -> item.getId())
            .toList()
            .forEach(cart::removeItem);

        cartRepository.save(cart);

        cart.getDomainEvents().forEach(eventPublisher::publish);
        cart.clearDomainEvents();
    }
}
