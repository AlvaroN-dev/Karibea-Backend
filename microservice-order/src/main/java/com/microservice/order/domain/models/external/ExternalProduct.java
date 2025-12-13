package com.microservice.order.domain.models.external;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ExternalProduct {
    private String name;
    private String description;
    private String brand;
    private String sku;
    private BigDecimal price;
    private String currency;
    private UUID idStatus;
    private Boolean isFeature;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
