package com.microservice.inventory.infrastructure.adapters.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.microservice.inventory.domain.models.Stock;
import com.microservice.inventory.domain.models.StockMovement;
import com.microservice.inventory.domain.models.enums.MovementType;
import com.microservice.inventory.infrastructure.entities.StockEntity;
import com.microservice.inventory.infrastructure.entities.StockMovementEntity;

@Component
public class StockEntityMapper {

    private final ReservationEntityMapper reservationMapper;

    public StockEntityMapper(ReservationEntityMapper reservationMapper) {
        this.reservationMapper = reservationMapper;
    }

    public Stock toDomain(StockEntity entity) {
        return Stock.builder()
                .id(entity.getId())
                .externalProductId(entity.getExternalProductId())
                .externalVariantId(entity.getExternalVariantId())
                .warehouseId(entity.getWarehouseId())
                .quantityAvailable(entity.getQuantityAvailable())
                .quantityReserved(entity.getQuantityReserved())
                .quantityIncoming(entity.getQuantityIncoming())
                .lowStockThreshold(entity.getLowStockThreshold())
                .reorderPoint(entity.getReorderPoint())
                .lastRestockedAt(entity.getLastRestockedAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .movements(entity.getMovements().stream()
                        .map(this::movementToDomain)
                        .collect(Collectors.toList()))
                .reservations(entity.getReservations().stream()
                        .map(reservationMapper::toDomain)
                        .collect(Collectors.toList()))
                .build();
    }

    public StockEntity toEntity(Stock stock) {
        StockEntity entity = new StockEntity();
        entity.setId(stock.getId());
        entity.setExternalProductId(stock.getExternalProductId());
        entity.setExternalVariantId(stock.getExternalVariantId());
        entity.setWarehouseId(stock.getWarehouseId());
        entity.setQuantityAvailable(stock.getQuantityAvailable());
        entity.setQuantityReserved(stock.getQuantityReserved());
        entity.setQuantityIncoming(stock.getQuantityIncoming());
        entity.setLowStockThreshold(stock.getLowStockThreshold());
        entity.setReorderPoint(stock.getReorderPoint());
        entity.setLastRestockedAt(stock.getLastRestockedAt());
        entity.setCreatedAt(stock.getCreatedAt());
        entity.setUpdatedAt(stock.getUpdatedAt());
        return entity;
    }

    private StockMovement movementToDomain(StockMovementEntity entity) {
        return StockMovement.builder()
                .id(entity.getId())
                .stockId(entity.getStock().getId())
                .movementType(MovementType.valueOf(entity.getMovementType()))
                .quantity(entity.getQuantity())
                .referenceType(entity.getReferenceType())
                .externalReferenceId(entity.getExternalReferenceId())
                .note(entity.getNote())
                .externalPerformedById(entity.getExternalPerformedById())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
