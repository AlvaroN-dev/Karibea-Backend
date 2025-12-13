package com.microservice.review.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Response DTO for paginated review lists.
 */
@Schema(description = "Paginated list of reviews")
public class ReviewListResponse {

    @Schema(description = "List of reviews")
    private List<ReviewResponse> reviews;

    @Schema(description = "Total number of elements", example = "150")
    private long totalElements;

    @Schema(description = "Total number of pages", example = "15")
    private int totalPages;

    @Schema(description = "Current page number (0-indexed)", example = "0")
    private int currentPage;

    @Schema(description = "Page size", example = "10")
    private int pageSize;

    @Schema(description = "Average rating for the product", example = "4.5")
    private Double averageRating;

    // Constructors
    public ReviewListResponse() {
    }

    public ReviewListResponse(List<ReviewResponse> reviews, long totalElements, int totalPages,
            int currentPage, int pageSize, Double averageRating) {
        this.reviews = reviews;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.averageRating = averageRating;
    }

    // Getters and Setters
    public List<ReviewResponse> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewResponse> reviews) {
        this.reviews = reviews;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
}
