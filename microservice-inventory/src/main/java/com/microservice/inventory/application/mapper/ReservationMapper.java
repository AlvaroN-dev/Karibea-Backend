package com.microservice.inventory.application.mapper;

import org.springframework.stereotype.Component;

import com.microservice.inventory.application.dto.ReservationResponse;
import com.microservice.inventory.domain.models.StockReservation;

@Component
public class ReservationMapper {

    public ReservationResponse toResponse(StockReservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getStockId(),
                reservation.getExternalOrderId(),
                reservation.getExternalCartId(),
                reservation.getQuantity(),
                reservation.getReservationType(),
                reservation.getStatus(),
                reservation.getExpiresAt(),
                reservation.getCreatedAt(),
                reservation.getUpdatedAt());
    }
}
