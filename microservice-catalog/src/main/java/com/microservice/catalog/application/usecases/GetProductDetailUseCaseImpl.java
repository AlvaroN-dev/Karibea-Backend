package com.microservice.catalog.application.usecases;

import com.microservice.catalog.domain.exceptions.ProductNotFoundException;
import com.microservice.catalog.domain.models.Product;
import com.microservice.catalog.domain.port.in.GetProductDetailUseCase;
import com.microservice.catalog.domain.port.out.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of GetProductDetailUseCase.
 */
@Service
public class GetProductDetailUseCaseImpl implements GetProductDetailUseCase {

    private static final Logger log = LoggerFactory.getLogger(GetProductDetailUseCaseImpl.class);

    private final ProductRepository productRepository;

    public GetProductDetailUseCaseImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Product execute(UUID productId) {
        log.debug("Getting product detail for ID: {}", productId);

        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }
}
