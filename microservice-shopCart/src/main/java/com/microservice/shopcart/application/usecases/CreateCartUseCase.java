package com.microservice.shopcart.application.usecases;

import com.microservice.shopcart.domain.models.ShoppingCart;
import com.microservice.shopcart.domain.port.in.CreateCartPort;
import com.microservice.shopcart.domain.port.out.CartRepositoryPort;
import com.microservice.shopcart.domain.port.out.EventPublisherPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Use case implementation for creating a new shopping cart.
 */
@Service
public class CreateCartUseCase implements CreateCartPort {

    private final CartRepositoryPort cartRepository;
    private final EventPublisherPort eventPublisher;

    public CreateCartUseCase(CartRepositoryPort cartRepository, 
                            EventPublisherPort eventPublisher) {
        this.cartRepository = cartRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public ShoppingCart execute(UUID userProfileId, String sessionId, String currency) {
        // Check if user already has an active cart
        if (userProfileId != null) {
            Optional<ShoppingCart> existingCart = cartRepository.findActiveByUserProfileId(userProfileId);
            if (existingCart.isPresent()) {
                return existingCart.get();
            }
        }

        // Check if session already has an active cart
        if (sessionId != null) {
            Optional<ShoppingCart> existingCart = cartRepository.findActiveBySessionId(sessionId);
            if (existingCart.isPresent()) {
                return existingCart.get();
            }
        }

        // Create new cart
        ShoppingCart cart = ShoppingCart.create(userProfileId, sessionId, currency);
        ShoppingCart savedCart = cartRepository.save(cart);

        // Publish domain events
        savedCart.getDomainEvents().forEach(eventPublisher::publish);
        savedCart.clearDomainEvents();

        return savedCart;
    }
}
