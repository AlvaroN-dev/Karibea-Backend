package com.microservice.review.domain.port.out;

import com.microservice.review.domain.models.ProductReview;
import com.microservice.review.domain.models.ReviewStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port for Review persistence operations.
 * Follows DIP - domain defines what it needs, infrastructure implements.
 */
public interface ReviewRepository {

    /**
     * Saves a review (create or update).
     */
    ProductReview save(ProductReview review);

    /**
     * Finds a review by ID.
     */
    Optional<ProductReview> findById(UUID id);

    /**
     * Finds all reviews for a product with pagination.
     */
    List<ProductReview> findByProductId(UUID productId, ReviewStatus status, int page, int size);

    /**
     * Finds all reviews by a user with pagination.
     */
    List<ProductReview> findByUserId(UUID userId, int page, int size);

    /**
     * Checks if a user has already reviewed a product.
     */
    boolean existsByUserIdAndProductId(UUID userId, UUID productId);

    /**
     * Counts reviews for a product.
     */
    long countByProductId(UUID productId, ReviewStatus status);

    /**
     * Counts reviews by a user.
     */
    long countByUserId(UUID userId);

    /**
     * Calculates the average rating for a product.
     */
    Double getAverageRatingByProductId(UUID productId);

    /**
     * Deletes a review by ID.
     */
    void deleteById(UUID id);

    /**
     * Checks if a review exists.
     */
    boolean existsById(UUID id);
}
