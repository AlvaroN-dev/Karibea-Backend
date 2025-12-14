package com.microservice.marketing.infrastructure.repositories;

import com.microservice.marketing.infrastructure.entities.PromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPromotionRepository extends JpaRepository<PromotionEntity, Long> {
}
