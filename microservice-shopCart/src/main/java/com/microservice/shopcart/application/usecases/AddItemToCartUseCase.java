package com.microservice.shopcart.application.usecases;

import com.microservice.shopcart.application.exception.ExternalServiceException;
import com.microservice.shopcart.domain.exceptions.CartNotFoundException;
import com.microservice.shopcart.domain.models.Item;
import com.microservice.shopcart.domain.models.Money;
import com.microservice.shopcart.domain.models.Quantity;
import com.microservice.shopcart.domain.models.ShoppingCart;
import com.microservice.shopcart.domain.port.in.AddItemPort;
import com.microservice.shopcart.domain.port.out.CartRepositoryPort;
import com.microservice.shopcart.domain.port.out.EventPublisherPort;
import com.microservice.shopcart.domain.port.out.ProductServicePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use case implementation for adding an item to a shopping cart.
 */
@Service
public class AddItemToCartUseCase implements AddItemPort {

    private final CartRepositoryPort cartRepository;
    private final ProductServicePort productService;
    private final EventPublisherPort eventPublisher;

    public AddItemToCartUseCase(CartRepositoryPort cartRepository,
                               ProductServicePort productService,
                               EventPublisherPort eventPublisher) {
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public void execute(UUID cartId, UUID productId, UUID variantId, UUID storeId, int quantity) {
        // Find cart
        ShoppingCart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new CartNotFoundException(cartId));

        // Fetch product info from Catalog service
        ProductServicePort.ProductInfo productInfo;
        try {
            productInfo = productService.getProduct(productId, variantId);
        } catch (Exception e) {
            throw new ExternalServiceException("Catalog", "Failed to fetch product information", e);
        }

        if (!productInfo.isAvailable()) {
            throw new ExternalServiceException("Catalog", "Product is not available");
        }

        // Create item
        Item item = Item.builder()
            .externalProductId(productInfo.productId())
            .externalVariantId(productInfo.variantId())
            .externalStoreId(storeId)
            .productName(productInfo.productName())
            .variantName(productInfo.variantName())
            .sku(productInfo.sku())
            .imageUrl(productInfo.imageUrl())
            .unitPrice(Money.of(productInfo.price(), cart.getCurrency()))
            .quantity(Quantity.of(quantity))
            .build();

        // Add item to cart (domain logic handles merging if exists)
        cart.addItem(item);

        // Save cart
        cartRepository.save(cart);

        // Publish domain events
        cart.getDomainEvents().forEach(eventPublisher::publish);
        cart.clearDomainEvents();
    }
}
