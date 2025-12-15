package com.microservice.marketing.infrastructure.repositories;

import com.microservice.marketing.infrastructure.entities.FlashSaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaFlashSaleRepository extends JpaRepository<FlashSaleEntity, UUID> {
}
