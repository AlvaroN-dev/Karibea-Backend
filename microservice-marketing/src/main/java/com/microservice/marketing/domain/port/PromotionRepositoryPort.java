package com.microservice.marketing.domain.port;

import com.microservice.marketing.domain.model.Promotion;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PromotionRepositoryPort {
    Promotion save(Promotion promotion);

    Optional<Promotion> findById(UUID id);

    List<Promotion> findAll();

    void deleteById(UUID id);
}
