package com.microservice.catalog.application.usecases;

import com.microservice.catalog.domain.events.ProductCreatedEvent;
import com.microservice.catalog.domain.exceptions.DuplicateSkuException;
import com.microservice.catalog.domain.models.Product;
import com.microservice.catalog.domain.port.in.CreateProductUseCase;
import com.microservice.catalog.domain.port.out.EventPublisher;
import com.microservice.catalog.domain.port.out.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of CreateProductUseCase.
 * Orchestrates product creation following the Single Responsibility Principle.
 */
@Service
public class CreateProductUseCaseImpl implements CreateProductUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateProductUseCaseImpl.class);

    private final ProductRepository productRepository;
    private final EventPublisher eventPublisher;

    public CreateProductUseCaseImpl(ProductRepository productRepository,
            EventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Product execute(CreateProductCommand command) {
        log.info("Creating product with SKU: {} for store: {}", command.sku(), command.storeId());

        // Check for duplicate SKU within the store
        if (productRepository.existsBySkuAndStoreId(command.sku(), command.storeId())) {
            throw new DuplicateSkuException(command.sku());
        }

        // Create the domain object using the factory method
        Product product = Product.create(
                command.storeId(),
                command.sku(),
                command.name(),
                command.description(),
                command.brand(),
                command.basePrice(),
                command.compareAtPrice(),
                command.currency());

        // Persist the product
        Product savedProduct = productRepository.save(product);

        // Publish domain event
        eventPublisher.publish(new ProductCreatedEvent(
                savedProduct.getId(),
                savedProduct.getExternalStoreId(),
                savedProduct.getSku(),
                savedProduct.getName()));

        log.info("Product created successfully with ID: {}", savedProduct.getId());

        return savedProduct;
    }
}
