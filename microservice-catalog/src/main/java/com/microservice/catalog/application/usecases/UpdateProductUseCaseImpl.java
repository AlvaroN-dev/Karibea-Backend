package com.microservice.catalog.application.usecases;

import com.microservice.catalog.domain.events.ProductUpdatedEvent;
import com.microservice.catalog.domain.exceptions.ProductNotFoundException;
import com.microservice.catalog.domain.models.Product;
import com.microservice.catalog.domain.port.in.UpdateProductUseCase;
import com.microservice.catalog.domain.port.out.EventPublisher;
import com.microservice.catalog.domain.port.out.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of UpdateProductUseCase.
 */
@Service
public class UpdateProductUseCaseImpl implements UpdateProductUseCase {

    private static final Logger log = LoggerFactory.getLogger(UpdateProductUseCaseImpl.class);

    private final ProductRepository productRepository;
    private final EventPublisher eventPublisher;

    public UpdateProductUseCaseImpl(ProductRepository productRepository,
            EventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Product execute(UpdateProductCommand command) {
        log.info("Updating product with ID: {}", command.productId());

        Product product = productRepository.findById(command.productId())
                .orElseThrow(() -> new ProductNotFoundException(command.productId()));

        // Update basic info if provided
        if (command.name() != null || command.description() != null || command.brand() != null) {
            product.updateBasicInfo(command.name(), command.description(), command.brand());
        }

        // Update pricing if provided
        if (command.basePrice() != null) {
            product.updatePricing(command.basePrice(), command.compareAtPrice(), command.currency());
        }

        // Update featured flag if provided
        if (command.featured() != null) {
            product.setFeatured(command.featured());
        }

        // Save changes
        Product savedProduct = productRepository.save(product);

        // Publish event
        eventPublisher.publish(new ProductUpdatedEvent(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getSku()));

        log.info("Product updated successfully: {}", savedProduct.getId());

        return savedProduct;
    }
}
