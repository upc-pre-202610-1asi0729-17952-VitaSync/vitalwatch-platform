package com.vitalwatch.center.platform.iam.domain.model.valueobjects;

/**
 * Peruvian RUC value object for hospital institutions.
 */
public record Ruc(String value) {

    public Ruc {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("RUC must not be null or blank");
        }
        if (!value.matches("\\d{11}")) {
            throw new IllegalArgumentException("RUC must contain exactly 11 digits");
        }
    }
}