package com.microservice.catalog.infrastructure.adapters;

import com.microservice.catalog.domain.models.*;
import com.microservice.catalog.domain.models.enums.ProductStatus;
import com.microservice.catalog.domain.port.out.ProductRepository;
import com.microservice.catalog.infrastructure.entities.*;
import com.microservice.catalog.infrastructure.repositories.JpaProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter implementing ProductRepository port using Spring Data JPA.
 * Follows the Adapter pattern from Hexagonal Architecture.
 */
@Component
public class ProductRepositoryAdapter implements ProductRepository {

    private final JpaProductRepository jpaRepository;

    public ProductRepositoryAdapter(JpaProductRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = toEntity(product);
        ProductEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return jpaRepository.findByIdWithDetails(id)
                .map(this::toDomain);
    }

    @Override
    public Optional<Product> findBySkuAndStoreId(String sku, UUID storeId) {
        return jpaRepository.findBySkuAndExternalStoreId(sku, storeId)
                .map(this::toDomain);
    }

    @Override
    public List<Product> findByStoreId(UUID storeId) {
        return jpaRepository.findByExternalStoreId(storeId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findAll(int page, int size) {
        Page<ProductEntity> pageResult = jpaRepository.findAll(PageRequest.of(page, size));
        return pageResult.getContent().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existsBySkuAndStoreId(String sku, UUID storeId) {
        return jpaRepository.existsBySkuAndExternalStoreId(sku, storeId);
    }

    // ========== Mapping Methods ==========

    private ProductEntity toEntity(Product product) {
        ProductEntity entity = new ProductEntity();
        entity.setId(product.getId());
        entity.setExternalStoreId(product.getExternalStoreId());
        entity.setSku(product.getSku());
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setBrand(product.getBrand());
        entity.setBasePrice(product.getBasePrice());
        entity.setCompareAtPrice(product.getCompareAtPrice());
        entity.setCurrency(product.getCurrency());
        entity.setStatus(ProductStatusEntity.valueOf(product.getStatus().name()));
        entity.setFeatured(product.isFeatured());
        entity.setCreatedAt(product.getCreatedAt());
        entity.setUpdatedAt(product.getUpdatedAt());
        entity.setVersion(product.getVersion());

        // Map variants
        for (Variant variant : product.getVariants()) {
            VariantEntity variantEntity = toVariantEntity(variant);
            entity.addVariant(variantEntity);
        }

        // Map images
        for (Image image : product.getImages()) {
            ImageEntity imageEntity = toImageEntity(image);
            entity.addImage(imageEntity);
        }

        // Map categories
        for (ProductCategory category : product.getCategories()) {
            ProductCategoryEntity categoryEntity = toCategoryEntity(category);
            categoryEntity.setProduct(entity);
            entity.getCategories().add(categoryEntity);
        }

        return entity;
    }

    private VariantEntity toVariantEntity(Variant variant) {
        VariantEntity entity = new VariantEntity();
        entity.setId(variant.getId());
        entity.setSku(variant.getSku());
        entity.setName(variant.getName());
        entity.setPrice(variant.getPrice());
        entity.setCompareAtPrice(variant.getCompareAtPrice());
        entity.setBarcode(variant.getBarcode());
        entity.setActive(variant.isActive());
        entity.setCreatedAt(variant.getCreatedAt());
        entity.setUpdatedAt(variant.getUpdatedAt());
        return entity;
    }

    private ImageEntity toImageEntity(Image image) {
        ImageEntity entity = new ImageEntity();
        entity.setId(image.getId());
        entity.setVariantId(image.getVariantId());
        entity.setUrl(image.getUrl());
        entity.setDisplayOrder(image.getDisplayOrder());
        entity.setPrimary(image.isPrimary());
        entity.setCreatedAt(image.getCreatedAt());
        return entity;
    }

    private ProductCategoryEntity toCategoryEntity(ProductCategory category) {
        ProductCategoryEntity entity = new ProductCategoryEntity();
        entity.setId(category.getId());
        entity.setCategoryId(category.getCategoryId());
        entity.setPrimary(category.isPrimary());
        return entity;
    }

    private Product toDomain(ProductEntity entity) {
        Product product = Product.reconstitute(
                entity.getId(),
                entity.getExternalStoreId(),
                entity.getSku(),
                entity.getName(),
                entity.getDescription(),
                entity.getBrand(),
                entity.getBasePrice(),
                entity.getCompareAtPrice(),
                entity.getCurrency(),
                ProductStatus.valueOf(entity.getStatus().name()),
                entity.isFeatured(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getVersion());

        // Map variants
        for (VariantEntity variantEntity : entity.getVariants()) {
            Variant variant = toVariantDomain(variantEntity);
            product.addVariantForReconstitution(variant);
        }

        // Map images
        for (ImageEntity imageEntity : entity.getImages()) {
            Image image = toImageDomain(imageEntity);
            product.addImageForReconstitution(image);
        }

        // Map categories
        for (ProductCategoryEntity categoryEntity : entity.getCategories()) {
            ProductCategory category = toCategoryDomain(categoryEntity, entity.getId());
            product.addCategoryForReconstitution(category);
        }

        return product;
    }

    private Variant toVariantDomain(VariantEntity entity) {
        Variant variant = Variant.reconstitute(
                entity.getId(),
                entity.getProduct().getId(),
                entity.getSku(),
                entity.getName(),
                entity.getPrice(),
                entity.getCompareAtPrice(),
                entity.getBarcode(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());

        // Map variant images
        for (ImageEntity imageEntity : entity.getImages()) {
            Image image = toImageDomain(imageEntity);
            variant.addImageForReconstitution(image);
        }

        return variant;
    }

    private Image toImageDomain(ImageEntity entity) {
        return Image.reconstitute(
                entity.getId(),
                entity.getProduct().getId(),
                entity.getVariantId(),
                entity.getUrl(),
                entity.getDisplayOrder(),
                entity.isPrimary(),
                entity.getCreatedAt());
    }

    private ProductCategory toCategoryDomain(ProductCategoryEntity entity, UUID productId) {
        return ProductCategory.reconstitute(
                entity.getId(),
                productId,
                entity.getCategoryId(),
                entity.isPrimary());
    }
}
