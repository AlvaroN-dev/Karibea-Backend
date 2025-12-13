package com.microservice.review.domain.models;

/**
 * Value Object representing a rating (1-5 stars).
 * Immutable and self-validating.
 */
public final class Rating {

    private static final int MIN_RATING = 1;
    private static final int MAX_RATING = 5;

    private final int value;

    private Rating(int value) {
        this.value = value;
    }

    /**
     * Factory method to create a Rating.
     * 
     * @param value the rating value (1-5)
     * @return a new Rating instance
     * @throws IllegalArgumentException if value is not between 1 and 5
     */
    public static Rating of(int value) {
        if (value < MIN_RATING || value > MAX_RATING) {
            throw new IllegalArgumentException(
                    String.format("Rating must be between %d and %d. Got: %d", MIN_RATING, MAX_RATING, value));
        }
        return new Rating(value);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Rating rating = (Rating) o;
        return value == rating.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
