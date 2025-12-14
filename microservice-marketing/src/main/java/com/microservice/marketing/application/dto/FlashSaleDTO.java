package com.microservice.marketing.application.dto;

import java.time.LocalDateTime;
import java.util.List;

public record FlashSaleDTO(
        Long id,
        String name,
        String description,
        String bannerUrl,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        Boolean isActive,
        List<FlashSaleProductDTO> products) {
}
