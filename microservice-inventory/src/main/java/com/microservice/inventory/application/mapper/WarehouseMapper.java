package com.microservice.inventory.application.mapper;

import org.springframework.stereotype.Component;

import com.microservice.inventory.application.dto.WarehouseResponse;
import com.microservice.inventory.domain.models.Warehouse;

@Component
public class WarehouseMapper {

    public WarehouseResponse toResponse(Warehouse warehouse) {
        return new WarehouseResponse(
                warehouse.getId(),
                warehouse.getExternalStoreId(),
                warehouse.getName(),
                warehouse.getCode(),
                warehouse.getAddress() != null ? warehouse.getAddress().getStreet() : null,
                warehouse.getAddress() != null ? warehouse.getAddress().getCity() : null,
                warehouse.getAddress() != null ? warehouse.getAddress().getCountry() : null,
                warehouse.isActive(),
                warehouse.getCreatedAt(),
                warehouse.getUpdatedAt());
    }
}
