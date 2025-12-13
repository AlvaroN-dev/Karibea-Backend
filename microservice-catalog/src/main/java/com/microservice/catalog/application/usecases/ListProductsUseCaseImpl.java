package com.microservice.catalog.application.usecases;

import com.microservice.catalog.domain.models.Product;
import com.microservice.catalog.domain.port.in.ListProductsUseCase;
import com.microservice.catalog.domain.port.out.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of ListProductsUseCase.
 */
@Service
public class ListProductsUseCaseImpl implements ListProductsUseCase {

    private static final Logger log = LoggerFactory.getLogger(ListProductsUseCaseImpl.class);

    private final ProductRepository productRepository;

    public ListProductsUseCaseImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ListProductsResult execute(ListProductsQuery query) {
        log.debug("Listing products for store: {}, page: {}, size: {}",
                query.storeId(), query.page(), query.size());

        List<Product> products;
        long totalElements;

        if (query.storeId() != null) {
            products = productRepository.findByStoreId(query.storeId());
            totalElements = products.size();

            // Apply pagination manually for store-specific queries
            int fromIndex = query.page() * query.size();
            int toIndex = Math.min(fromIndex + query.size(), products.size());

            if (fromIndex < products.size()) {
                products = products.subList(fromIndex, toIndex);
            } else {
                products = List.of();
            }
        } else {
            products = productRepository.findAll(query.page(), query.size());
            totalElements = productRepository.count();
        }

        int totalPages = (int) Math.ceil((double) totalElements / query.size());

        return new ListProductsResult(products, totalElements, totalPages, query.page());
    }
}
