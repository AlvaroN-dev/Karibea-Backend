package com.microservice.search.infrastructure.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Entidad JPA para el índice de productos buscables.
 */
@Entity
@Table(name = "product_index", indexes = {
        @Index(name = "idx_product_external_id", columnList = "external_product_id"),
        @Index(name = "idx_product_store", columnList = "external_store_id"),
        @Index(name = "idx_product_name", columnList = "name"),
        @Index(name = "idx_product_brand", columnList = "brand"),
        @Index(name = "idx_product_active", columnList = "is_active")
})
public class ProductIndexEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_index", updatable = false, nullable = false)
    private UUID idIndex;

    @Column(name = "external_product_id", nullable = false, unique = true)
    private UUID externalProductId;

    @Column(name = "external_store_id", nullable = false)
    private UUID externalStoreId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "brand", length = 100)
    private String brand;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "category_names", columnDefinition = "jsonb")
    private List<String> categoryNames;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "external_category_ids", columnDefinition = "jsonb")
    private List<UUID> externalCategoryIds;

    @Column(name = "price", precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "compare_at_price", precision = 12, scale = 2)
    private BigDecimal compareAtPrice;

    @Column(name = "currency", length = 3)
    private String currency;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "colors", columnDefinition = "jsonb")
    private List<String> colors;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "sizes", columnDefinition = "jsonb")
    private List<String> sizes;

    @Column(name = "average_rating", precision = 3, scale = 2)
    private BigDecimal averageRating;

    @Column(name = "review_count")
    private Integer reviewCount;

    @Column(name = "sales_count")
    private Integer salesCount;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(name = "primary_image_url", length = 500)
    private String primaryImageUrl;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "image_urls", columnDefinition = "jsonb")
    private List<String> imageUrls;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "indexed_at")
    private Instant indexedAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public ProductIndexEntity() {
    }

    @PrePersist
    protected void onCreate() {
        if (indexedAt == null) {
            indexedAt = Instant.now();
        }
        if (isActive == null) {
            isActive = true;
        }
        if (isDeleted == null) {
            isDeleted = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // Getters and Setters
    public UUID getIdIndex() { return idIndex; }
    public void setIdIndex(UUID idIndex) { this.idIndex = idIndex; }

    public UUID getExternalProductId() { return externalProductId; }
    public void setExternalProductId(UUID externalProductId) { this.externalProductId = externalProductId; }

    public UUID getExternalStoreId() { return externalStoreId; }
    public void setExternalStoreId(UUID externalStoreId) { this.externalStoreId = externalStoreId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public List<String> getCategoryNames() { return categoryNames; }
    public void setCategoryNames(List<String> categoryNames) { this.categoryNames = categoryNames; }

    public List<UUID> getExternalCategoryIds() { return externalCategoryIds; }
    public void setExternalCategoryIds(List<UUID> externalCategoryIds) { this.externalCategoryIds = externalCategoryIds; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getCompareAtPrice() { return compareAtPrice; }
    public void setCompareAtPrice(BigDecimal compareAtPrice) { this.compareAtPrice = compareAtPrice; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public List<String> getColors() { return colors; }
    public void setColors(List<String> colors) { this.colors = colors; }

    public List<String> getSizes() { return sizes; }
    public void setSizes(List<String> sizes) { this.sizes = sizes; }

    public BigDecimal getAverageRating() { return averageRating; }
    public void setAverageRating(BigDecimal averageRating) { this.averageRating = averageRating; }

    public Integer getReviewCount() { return reviewCount; }
    public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }

    public Integer getSalesCount() { return salesCount; }
    public void setSalesCount(Integer salesCount) { this.salesCount = salesCount; }

    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }

    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }

    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }

    public String getPrimaryImageUrl() { return primaryImageUrl; }
    public void setPrimaryImageUrl(String primaryImageUrl) { this.primaryImageUrl = primaryImageUrl; }

    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Instant getIndexedAt() { return indexedAt; }
    public void setIndexedAt(Instant indexedAt) { this.indexedAt = indexedAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public Instant getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Instant deletedAt) { this.deletedAt = deletedAt; }

    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }
}
