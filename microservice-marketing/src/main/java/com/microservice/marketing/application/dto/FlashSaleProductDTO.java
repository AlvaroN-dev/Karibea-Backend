package com.microservice.marketing.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record FlashSaleProductDTO(
        UUID id,
        UUID flashSaleId,
        String externalProductId,
        BigDecimal salePrice,
        Integer quantityLimit,
        Integer quantitySold) {
}
