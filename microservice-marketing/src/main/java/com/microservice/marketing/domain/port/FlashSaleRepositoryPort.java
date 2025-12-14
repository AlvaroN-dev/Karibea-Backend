package com.microservice.marketing.domain.port;

import com.microservice.marketing.domain.model.FlashSale;
import java.util.List;
import java.util.Optional;

public interface FlashSaleRepositoryPort {
    FlashSale save(FlashSale flashSale);

    Optional<FlashSale> findById(Long id);

    List<FlashSale> findAll();

    void deleteById(Long id);
}
