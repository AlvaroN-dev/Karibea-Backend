package com.microservice.shoppingcart.infrastructure.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "items")
public class EntityItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
}
