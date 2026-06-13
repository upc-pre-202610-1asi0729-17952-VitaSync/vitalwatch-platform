package com.vitalwatch.center.platform.subscriptions.domain.model.valueobjects;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Money value object for subscription plan pricing.
 */
public record Money(BigDecimal amount, String currency) {

    public Money {
        Objects.requireNonNull(amount, "amount must not be null");

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Money amount must not be negative");
        }

        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency must not be null or blank");
        }

        currency = currency.trim().toUpperCase();

        if (currency.length() != 3) {
            throw new IllegalArgumentException("Currency must use ISO 4217 format with 3 letters");
        }
    }
}