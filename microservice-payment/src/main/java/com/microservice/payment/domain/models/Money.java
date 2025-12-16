package com.microservice.payment.domain.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value Object representing monetary amounts with currency.
 * Immutable and self-validating to ensure financial data integrity.
 * 
 * <p>
 * Design decisions:
 * </p>
 * <ul>
 * <li>BigDecimal for precision (no floating-point errors)</li>
 * <li>Scale of 2 for cents-level accuracy</li>
 * <li>Immutable to prevent accidental modifications</li>
 * </ul>
 */
public final class Money {

    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    private final BigDecimal amount;
    private final String currency;

    private Money(BigDecimal amount, String currency) {
        this.amount = amount.setScale(SCALE, ROUNDING_MODE);
        this.currency = currency;
    }

    public static Money of(BigDecimal amount, String currency) {
        validateAmount(amount);
        validateCurrency(currency);
        return new Money(amount, currency);
    }

    public static Money of(double amount, String currency) {
        return of(BigDecimal.valueOf(amount), currency);
    }

    public static Money zero(String currency) {
        validateCurrency(currency);
        return new Money(BigDecimal.ZERO, currency);
    }

    private static void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative: " + amount);
        }
    }

    private static void validateCurrency(String currency) {
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency cannot be null or empty");
        }
        if (currency.length() != 3) {
            throw new IllegalArgumentException("Currency must be a 3-letter ISO 4217 code: " + currency);
        }
    }

    public Money add(Money other) {
        validateSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        validateSameCurrency(other);
        BigDecimal result = this.amount.subtract(other.amount);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Subtraction would result in negative amount");
        }
        return new Money(result, this.currency);
    }

    public boolean isGreaterThan(Money other) {
        validateSameCurrency(other);
        return this.amount.compareTo(other.amount) > 0;
    }

    public boolean isGreaterThanOrEqual(Money other) {
        validateSameCurrency(other);
        return this.amount.compareTo(other.amount) >= 0;
    }

    public boolean isLessThan(Money other) {
        validateSameCurrency(other);
        return this.amount.compareTo(other.amount) < 0;
    }

    public boolean isZero() {
        return this.amount.compareTo(BigDecimal.ZERO) == 0;
    }

    public boolean isPositive() {
        return this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    private void validateSameCurrency(Money other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot perform operation with null Money");
        }
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                    String.format("Currency mismatch: %s vs %s", this.currency, other.currency));
        }
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Money money = (Money) o;
        return amount.compareTo(money.amount) == 0 &&
                Objects.equals(currency, money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount.stripTrailingZeros(), currency);
    }

    @Override
    public String toString() {
        return String.format("%s %s", currency, amount.toPlainString());
    }
}
