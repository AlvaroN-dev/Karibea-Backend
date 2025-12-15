package com.microservice.marketing.domain.port;

import com.microservice.marketing.domain.model.FlashSale;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FlashSaleRepositoryPort {
    FlashSale save(FlashSale flashSale);

    Optional<FlashSale> findById(UUID id);

    List<FlashSale> findAll();

    void deleteById(UUID id);
}
