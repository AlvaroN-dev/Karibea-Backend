package com.microservice.shoppingcart.infrastructure.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shopping_carts")
public class ShoppingCartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long externalUserProfilesId;
    private String sessionId;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private StatusEntity status;

    private String currency;
    private BigDecimal subtotal;
    private BigDecimal total;
    private Integer itemCount;
    @Column(columnDefinition = "text")
    private String notes;
    private Instant createAt;
    private Instant updateAt;
    private Instant expiredAt;
    private Boolean isDelete;

    @OneToMany(mappedBy = "shoppingCarts", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemEntity> items = new ArrayList<>();

    @OneToMany(mappedBy = "shoppingCarts", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CouponAppliedEntity> coupons = new ArrayList<>();
}
