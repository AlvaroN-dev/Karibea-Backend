package com.microservice.marketing.infrastructure.adapter.mapper;

import com.microservice.marketing.domain.model.FlashSale;
import com.microservice.marketing.domain.model.FlashSaleProduct;
import com.microservice.marketing.infrastructure.entities.FlashSaleEntity;
import com.microservice.marketing.infrastructure.entities.FlashSaleProductEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FlashSaleEntityMapper {

    public FlashSale toDomain(FlashSaleEntity entity) {
        if (entity == null)
            return null;
        List<FlashSaleProduct> products = new ArrayList<>();
        if (entity.getProducts() != null) {
            products = entity.getProducts().stream()
                    .map(this::toProductDomain)
                    .collect(Collectors.toList());
        }
        return new FlashSale(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getBannerUrl(),
                entity.getStartedAt(),
                entity.getEndedAt(),
                entity.getIsActive(),
                entity.getCreatedAt(),
                products);
    }

    public FlashSaleEntity toEntity(FlashSale domain) {
        if (domain == null)
            return null;
        FlashSaleEntity entity = new FlashSaleEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setBannerUrl(domain.getBannerUrl());
        entity.setStartedAt(domain.getStartedAt());
        entity.setEndedAt(domain.getEndedAt());
        entity.setIsActive(domain.getIsActive());
        entity.setCreatedAt(domain.getCreatedAt());

        if (domain.getProducts() != null) {
            List<FlashSaleProductEntity> productEntities = domain.getProducts().stream()
                    .map(p -> toProductEntity(p, entity))
                    .collect(Collectors.toList());
            entity.setProducts(productEntities);
        }
        return entity;
    }

    private FlashSaleProduct toProductDomain(FlashSaleProductEntity entity) {
        if (entity == null)
            return null;
        return new FlashSaleProduct(
                entity.getId(),
                entity.getFlashSale() != null ? entity.getFlashSale().getId() : null,
                entity.getExternalProductId(),
                entity.getSalePrice(),
                entity.getQuantityLimit(),
                entity.getQuantitySold());
    }

    private FlashSaleProductEntity toProductEntity(FlashSaleProduct domain, FlashSaleEntity parent) {
        if (domain == null)
            return null;
        FlashSaleProductEntity entity = new FlashSaleProductEntity();
        entity.setId(domain.getId());
        entity.setFlashSale(parent);
        entity.setExternalProductId(domain.getExternalProductId());
        entity.setSalePrice(domain.getSalePrice());
        entity.setQuantityLimit(domain.getQuantityLimit());
        entity.setQuantitySold(domain.getQuantitySold());
        return entity;
    }
}
