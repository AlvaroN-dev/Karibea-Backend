package com.microservice.inventory.infrastructure.adapters.mapper;

import org.springframework.stereotype.Component;

import com.microservice.inventory.domain.models.StockReservation;
import com.microservice.inventory.domain.models.enums.ReservationStatus;
import com.microservice.inventory.domain.models.enums.ReservationType;
import com.microservice.inventory.infrastructure.entities.StockReservationEntity;

@Component
public class ReservationEntityMapper {

    public StockReservation toDomain(StockReservationEntity entity) {
        return StockReservation.builder()
                .id(entity.getId())
                .stockId(entity.getStock().getId())
                .externalOrderId(entity.getExternalOrderId())
                .externalCartId(entity.getExternalCartId())
                .quantity(entity.getQuantity())
                .reservationType(ReservationType.valueOf(entity.getReservationType()))
                .status(ReservationStatus.valueOf(entity.getStatus()))
                .expiresAt(entity.getExpiresAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public StockReservationEntity toEntity(StockReservation reservation) {
        StockReservationEntity entity = new StockReservationEntity();
        entity.setId(reservation.getId());
        entity.setExternalOrderId(reservation.getExternalOrderId());
        entity.setExternalCartId(reservation.getExternalCartId());
        entity.setQuantity(reservation.getQuantity());
        entity.setReservationType(reservation.getReservationType().name());
        entity.setStatus(reservation.getStatus().name());
        entity.setExpiresAt(reservation.getExpiresAt());
        entity.setCreatedAt(reservation.getCreatedAt());
        entity.setUpdatedAt(reservation.getUpdatedAt());
        return entity;
    }
}
