package com.microservice.catalog.application.mapper;

import com.microservice.catalog.application.dto.ImageResponse;
import com.microservice.catalog.application.dto.ProductResponse;
import com.microservice.catalog.application.dto.VariantResponse;
import com.microservice.catalog.domain.models.Image;
import com.microservice.catalog.domain.models.Product;
import com.microservice.catalog.domain.models.Variant;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class for converting between domain models and DTOs.
 * Follows the Single Responsibility Principle (SRP) - only handles mapping.
 */
@Component
public class ProductMapper {

    /**
     * Converts a Product domain model to ProductResponse DTO.
     *
     * @param product the domain model
     * @return the response DTO
     */
    public ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        }

        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setExternalStoreId(product.getExternalStoreId());
        response.setSku(product.getSku());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setBrand(product.getBrand());
        response.setBasePrice(product.getBasePrice());
        response.setCompareAtPrice(product.getCompareAtPrice());
        response.setCurrency(product.getCurrency());
        response.setStatus(product.getStatus().name());
        response.setFeatured(product.isFeatured());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        response.setVariants(toVariantResponseList(product.getVariants()));
        response.setImages(toImageResponseList(product.getImages()));

        return response;
    }

    /**
     * Converts a list of Product domain models to ProductResponse DTOs.
     *
     * @param products the list of domain models
     * @return the list of response DTOs
     */
    public List<ProductResponse> toResponseList(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return Collections.emptyList();
        }

        return products.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Converts a Variant domain model to VariantResponse DTO.
     *
     * @param variant the domain model
     * @return the response DTO
     */
    public VariantResponse toVariantResponse(Variant variant) {
        if (variant == null) {
            return null;
        }

        VariantResponse response = new VariantResponse();
        response.setId(variant.getId());
        response.setProductId(variant.getProductId());
        response.setSku(variant.getSku());
        response.setName(variant.getName());
        response.setPrice(variant.getPrice());
        response.setCompareAtPrice(variant.getCompareAtPrice());
        response.setBarcode(variant.getBarcode());
        response.setActive(variant.isActive());
        response.setCreatedAt(variant.getCreatedAt());
        response.setUpdatedAt(variant.getUpdatedAt());
        response.setImages(toImageResponseList(variant.getImages()));

        return response;
    }

    /**
     * Converts a list of Variant domain models to VariantResponse DTOs.
     *
     * @param variants the list of domain models
     * @return the list of response DTOs
     */
    public List<VariantResponse> toVariantResponseList(List<Variant> variants) {
        if (variants == null || variants.isEmpty()) {
            return Collections.emptyList();
        }

        return variants.stream()
                .map(this::toVariantResponse)
                .collect(Collectors.toList());
    }

    /**
     * Converts an Image domain model to ImageResponse DTO.
     *
     * @param image the domain model
     * @return the response DTO
     */
    public ImageResponse toImageResponse(Image image) {
        if (image == null) {
            return null;
        }

        return new ImageResponse(
                image.getId(),
                image.getUrl(),
                image.getDisplayOrder(),
                image.isPrimary(),
                image.getCreatedAt());
    }

    /**
     * Converts a list of Image domain models to ImageResponse DTOs.
     *
     * @param images the list of domain models
     * @return the list of response DTOs
     */
    public List<ImageResponse> toImageResponseList(List<Image> images) {
        if (images == null || images.isEmpty()) {
            return Collections.emptyList();
        }

        return images.stream()
                .map(this::toImageResponse)
                .collect(Collectors.toList());
    }
}
