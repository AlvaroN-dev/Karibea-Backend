package com.microservice.catalog.application.usecases;

import com.microservice.catalog.domain.events.ProductDeactivatedEvent;
import com.microservice.catalog.domain.exceptions.ProductNotFoundException;
import com.microservice.catalog.domain.models.Product;
import com.microservice.catalog.domain.port.in.DeactivateProductUseCase;
import com.microservice.catalog.domain.port.out.EventPublisher;
import com.microservice.catalog.domain.port.out.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of DeactivateProductUseCase.
 */
@Service
public class DeactivateProductUseCaseImpl implements DeactivateProductUseCase {

    private static final Logger log = LoggerFactory.getLogger(DeactivateProductUseCaseImpl.class);

    private final ProductRepository productRepository;
    private final EventPublisher eventPublisher;

    public DeactivateProductUseCaseImpl(ProductRepository productRepository,
            EventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Product execute(UUID productId) {
        log.info("Deactivating product with ID: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        // Deactivate the product
        product.deactivate();

        // Save changes
        Product savedProduct = productRepository.save(product);

        // Publish event
        eventPublisher.publish(new ProductDeactivatedEvent(
                savedProduct.getId(),
                savedProduct.getExternalStoreId()));

        log.info("Product deactivated successfully: {}", savedProduct.getId());

        return savedProduct;
    }
}
