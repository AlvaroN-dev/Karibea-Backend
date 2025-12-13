package com.microservice.catalog.application.usecases;

import com.microservice.catalog.domain.events.ProductPublishedEvent;
import com.microservice.catalog.domain.exceptions.ProductNotFoundException;
import com.microservice.catalog.domain.models.Product;
import com.microservice.catalog.domain.port.in.PublishProductUseCase;
import com.microservice.catalog.domain.port.out.EventPublisher;
import com.microservice.catalog.domain.port.out.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of PublishProductUseCase.
 */
@Service
public class PublishProductUseCaseImpl implements PublishProductUseCase {

    private static final Logger log = LoggerFactory.getLogger(PublishProductUseCaseImpl.class);

    private final ProductRepository productRepository;
    private final EventPublisher eventPublisher;

    public PublishProductUseCaseImpl(ProductRepository productRepository,
            EventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Product execute(UUID productId) {
        log.info("Publishing product with ID: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        // Apply business rule - this will throw if invariants are not met
        product.publish();

        // Save changes
        Product savedProduct = productRepository.save(product);

        // Publish event
        eventPublisher.publish(new ProductPublishedEvent(
                savedProduct.getId(),
                savedProduct.getExternalStoreId(),
                savedProduct.getSku()));

        log.info("Product published successfully: {}", savedProduct.getId());

        return savedProduct;
    }
}
