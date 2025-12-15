package com.microservice.marketing.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record FlashSaleDTO(
        UUID id,
        String name,
        String description,
        String bannerUrl,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        Boolean isActive,
        List<FlashSaleProductDTO> products) {
}
