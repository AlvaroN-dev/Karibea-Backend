package com.microservice.shopcart.application.usecases;

import com.microservice.shopcart.domain.models.ShoppingCart;
import com.microservice.shopcart.domain.port.in.GetCartPort;
import com.microservice.shopcart.domain.port.out.CartRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Use case implementation for getting shopping cart details.
 */
@Service
public class GetCartUseCase implements GetCartPort {

    private final CartRepositoryPort cartRepository;

    public GetCartUseCase(CartRepositoryPort cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShoppingCart> execute(UUID cartId) {
        return cartRepository.findById(cartId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShoppingCart> findByUserProfileId(UUID userProfileId) {
        return cartRepository.findActiveByUserProfileId(userProfileId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShoppingCart> findBySessionId(String sessionId) {
        return cartRepository.findActiveBySessionId(sessionId);
    }
}
