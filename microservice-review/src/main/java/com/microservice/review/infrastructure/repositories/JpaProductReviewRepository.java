package com.microservice.review.infrastructure.repositories;

import com.microservice.review.infrastructure.entities.ProductReviewEntity;
import com.microservice.review.infrastructure.entities.ReviewStatusEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA Repository for ProductReviewEntity.
 */
@Repository
public interface JpaProductReviewRepository extends JpaRepository<ProductReviewEntity, UUID> {

    @Query("SELECT r FROM ProductReviewEntity r LEFT JOIN FETCH r.images LEFT JOIN FETCH r.votes WHERE r.id = :id")
    Optional<ProductReviewEntity> findByIdWithDetails(@Param("id") UUID id);

    Page<ProductReviewEntity> findByExternalProductIdAndStatusAndDeletedFalse(
            UUID externalProductId, ReviewStatusEntity status, Pageable pageable);

    Page<ProductReviewEntity> findByExternalUserProfileIdAndDeletedFalse(
            UUID externalUserProfileId, Pageable pageable);

    boolean existsByExternalUserProfileIdAndExternalProductIdAndDeletedFalse(
            UUID externalUserProfileId, UUID externalProductId);

    long countByExternalProductIdAndStatusAndDeletedFalse(
            UUID externalProductId, ReviewStatusEntity status);

    long countByExternalUserProfileIdAndDeletedFalse(UUID externalUserProfileId);

    @Query("SELECT AVG(r.rating) FROM ProductReviewEntity r WHERE r.externalProductId = :productId AND r.status = 'APPROVED' AND r.deleted = false")
    Double getAverageRatingByProductId(@Param("productId") UUID productId);
}
