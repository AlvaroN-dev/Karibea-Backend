package com.microservice.inventory.application.usecases;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.inventory.domain.exceptions.ReservationNotFoundException;
import com.microservice.inventory.domain.exceptions.StockNotFoundException;
import com.microservice.inventory.domain.models.Stock;
import com.microservice.inventory.domain.models.StockReservation;
import com.microservice.inventory.domain.port.in.ConfirmReservationUseCase;
import com.microservice.inventory.domain.port.out.StockRepositoryPort;
import com.microservice.inventory.domain.port.out.StockReservationRepositoryPort;

@Service
@Transactional
public class ConfirmReservationService implements ConfirmReservationUseCase {

    private final StockRepositoryPort stockRepository;
    private final StockReservationRepositoryPort reservationRepository;

    public ConfirmReservationService(StockRepositoryPort stockRepository,
            StockReservationRepositoryPort reservationRepository) {
        this.stockRepository = stockRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void execute(ConfirmReservationCommand command) {
        StockReservation reservation = reservationRepository.findById(command.reservationId())
                .orElseThrow(() -> new ReservationNotFoundException(command.reservationId()));

        Stock stock = stockRepository.findById(reservation.getStockId())
                .orElseThrow(() -> new StockNotFoundException(reservation.getStockId()));

        stock.confirmReservation(reservation, command.externalOrderId(), command.performedById());

        stockRepository.save(stock);
        reservationRepository.save(reservation);
    }
}
