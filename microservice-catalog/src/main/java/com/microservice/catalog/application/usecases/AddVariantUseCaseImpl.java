package com.microservice.catalog.application.usecases;

import com.microservice.catalog.domain.events.VariantAddedEvent;
import com.microservice.catalog.domain.exceptions.ProductNotFoundException;
import com.microservice.catalog.domain.models.Product;
import com.microservice.catalog.domain.models.Variant;
import com.microservice.catalog.domain.port.in.AddVariantUseCase;
import com.microservice.catalog.domain.port.out.EventPublisher;
import com.microservice.catalog.domain.port.out.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of AddVariantUseCase.
 */
@Service
public class AddVariantUseCaseImpl implements AddVariantUseCase {

    private static final Logger log = LoggerFactory.getLogger(AddVariantUseCaseImpl.class);

    private final ProductRepository productRepository;
    private final EventPublisher eventPublisher;

    public AddVariantUseCaseImpl(ProductRepository productRepository,
            EventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Product execute(AddVariantCommand command) {
        log.info("Adding variant with SKU: {} to product: {}", command.sku(), command.productId());

        Product product = productRepository.findById(command.productId())
                .orElseThrow(() -> new ProductNotFoundException(command.productId()));

        // Create the variant
        Variant variant = Variant.create(
                command.productId(),
                command.sku(),
                command.name(),
                command.price(),
                command.compareAtPrice(),
                command.barcode());

        // Add variant to product (this validates SKU uniqueness within product)
        product.addVariant(variant);

        // Save changes
        Product savedProduct = productRepository.save(product);

        // Publish event
        eventPublisher.publish(new VariantAddedEvent(
                savedProduct.getId(),
                variant.getId(),
                variant.getSku(),
                variant.getPrice()));

        log.info("Variant added successfully: {}", variant.getId());

        return savedProduct;
    }
}
