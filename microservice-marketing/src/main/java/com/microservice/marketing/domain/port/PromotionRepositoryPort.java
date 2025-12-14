package com.microservice.marketing.domain.port;

import com.microservice.marketing.domain.model.Promotion;
import java.util.List;
import java.util.Optional;

public interface PromotionRepositoryPort {
    Promotion save(Promotion promotion);

    Optional<Promotion> findById(Long id);

    List<Promotion> findAll();

    void deleteById(Long id);
}
