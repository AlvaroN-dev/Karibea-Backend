package com.microservice.marketing.application.dto;

import java.math.BigDecimal;

public record FlashSaleProductDTO(
        Long id,
        Long flashSaleId,
        String externalProductId,
        BigDecimal salePrice,
        Integer quantityLimit,
        Integer quantitySold) {
}
