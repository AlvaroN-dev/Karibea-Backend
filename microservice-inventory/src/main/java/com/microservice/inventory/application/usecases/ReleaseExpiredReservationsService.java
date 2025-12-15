package com.microservice.inventory.application.usecases;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.inventory.domain.models.Stock;
import com.microservice.inventory.domain.models.StockReservation;
import com.microservice.inventory.domain.port.in.ReleaseExpiredReservationsUseCase;
import com.microservice.inventory.domain.port.out.EventPublisherPort;
import com.microservice.inventory.domain.port.out.StockRepositoryPort;
import com.microservice.inventory.domain.port.out.StockReservationRepositoryPort;

@Service
@Transactional
public class ReleaseExpiredReservationsService implements ReleaseExpiredReservationsUseCase {

    private static final Logger log = LoggerFactory.getLogger(ReleaseExpiredReservationsService.class);

    private final StockRepositoryPort stockRepository;
    private final StockReservationRepositoryPort reservationRepository;
    private final EventPublisherPort eventPublisher;

    public ReleaseExpiredReservationsService(StockRepositoryPort stockRepository,
            StockReservationRepositoryPort reservationRepository,
            EventPublisherPort eventPublisher) {
        this.stockRepository = stockRepository;
        this.reservationRepository = reservationRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Scheduled(fixedRateString = "${inventory.reservation.expiry-check-interval:60000}")
    public int execute() {
        List<StockReservation> expiredReservations = reservationRepository
                .findExpiredReservations(LocalDateTime.now());

        int count = 0;
        for (StockReservation reservation : expiredReservations) {
            try {
                Stock stock = stockRepository.findById(reservation.getStockId()).orElse(null);
                if (stock != null) {
                    stock.releaseReservation(reservation, "Reservation expired");
                    stockRepository.save(stock);
                    reservationRepository.save(reservation);
                    eventPublisher.publishAll(stock.getDomainEvents());
                    stock.clearDomainEvents();
                    count++;
                }
            } catch (Exception e) {
                log.error("Failed to release expired reservation: {}", reservation.getId(), e);
            }
        }

        if (count > 0) {
            log.info("Released {} expired reservations", count);
        }

        return count;
    }
}
