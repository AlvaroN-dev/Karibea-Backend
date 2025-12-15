package com.microservice.search.infrastructure.adapters;

import com.microservice.search.domain.models.ProductId;
import com.microservice.search.domain.models.SearchableProduct;
import com.microservice.search.domain.port.out.ProductPersistencePort;
import com.microservice.search.infrastructure.entities.ProductIndexEntity;
import com.microservice.search.infrastructure.repositories.ProductIndexRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador de PostgreSQL para persistencia de productos.
 */
@Component
public class PostgresPersistenceAdapter implements ProductPersistencePort {

    private final ProductIndexRepository repository;

    public PostgresPersistenceAdapter(ProductIndexRepository repository) {
        this.repository = repository;
    }

    @Override
    public SearchableProduct save(SearchableProduct product) {
        ProductIndexEntity entity = repository.findByExternalProductId(product.id().value())
                .orElse(new ProductIndexEntity());

        mapToEntity(product, entity);
        ProductIndexEntity saved = repository.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public List<SearchableProduct> saveAll(List<SearchableProduct> products) {
        return products.stream()
                .map(this::save)
                .toList();
    }

    @Override
    public Optional<SearchableProduct> findByExternalProductId(UUID productId) {
        return repository.findByExternalProductId(productId)
                .filter(e -> !Boolean.TRUE.equals(e.getIsDeleted()))
                .map(this::mapToDomain);
    }

    @Override
    public List<SearchableProduct> findByStoreId(UUID storeId) {
        return repository.findByExternalStoreIdAndIsActiveTrueAndIsDeletedFalse(storeId)
                .stream()
                .map(this::mapToDomain)
                .toList();
    }

    @Override
    public void delete(ProductId productId) {
        repository.findByExternalProductId(productId.value())
                .ifPresent(entity -> {
                    entity.setIsDeleted(true);
                    entity.setDeletedAt(Instant.now());
                    entity.setIsActive(false);
                    repository.save(entity);
                });
    }

    @Override
    public List<SearchableProduct> findAllActive() {
        return repository.findAllActive()
                .stream()
                .map(this::mapToDomain)
                .toList();
    }

    @Override
    public boolean existsByExternalProductId(UUID productId) {
        return repository.existsByExternalProductId(productId);
    }

    private void mapToEntity(SearchableProduct p, ProductIndexEntity e) {
        e.setExternalProductId(p.id().value());
        e.setExternalStoreId(p.storeId());
        e.setName(p.name());
        e.setDescription(p.description());
        e.setBrand(p.brand());
        e.setCategoryNames(p.categoryNames());
        e.setExternalCategoryIds(p.categoryIds());
        e.setPrice(p.price());
        e.setCompareAtPrice(p.compareAtPrice());
        e.setCurrency(p.currency());
        e.setColors(p.colors());
        e.setSizes(p.sizes());
        e.setAverageRating(p.averageRating());
        e.setReviewCount(p.reviewCount());
        e.setSalesCount(p.salesCount());
        e.setViewCount(p.viewCount());
        e.setIsAvailable(p.isAvailable());
        e.setStockQuantity(p.stockQuantity());
        e.setPrimaryImageUrl(p.primaryImageUrl());
        e.setImageUrls(p.imageUrls());
        e.setIsActive(p.isActive());
        e.setIsDeleted(p.isDeleted());
    }

    private SearchableProduct mapToDomain(ProductIndexEntity e) {
        return new SearchableProduct(
                new ProductId(e.getExternalProductId()),
                e.getExternalStoreId(),
                e.getName(),
                e.getDescription(),
                e.getBrand(),
                e.getCategoryNames(),
                e.getExternalCategoryIds(),
                e.getPrice(),
                e.getCompareAtPrice(),
                e.getCurrency(),
                e.getColors(),
                e.getSizes(),
                e.getAverageRating(),
                e.getReviewCount(),
                e.getSalesCount(),
                e.getViewCount(),
                Boolean.TRUE.equals(e.getIsAvailable()),
                e.getStockQuantity(),
                e.getPrimaryImageUrl(),
                e.getImageUrls(),
                Boolean.TRUE.equals(e.getIsActive()),
                e.getIndexedAt(),
                e.getUpdatedAt(),
                Boolean.TRUE.equals(e.getIsDeleted()));
    }
}
