package com.vitalwatch.center.platform.staffrecovery.domain.model.valueobjects;

/**
 * Recommended rest hours for staff recovery.
 */
public record RestHours(Double value) {

    public RestHours {
        if (value == null) {
            throw new IllegalArgumentException("Rest hours must not be null");
        }

        if (value < 0) {
            throw new IllegalArgumentException("Rest hours must not be negative");
        }

        if (value > 72) {
            throw new IllegalArgumentException("Rest hours must not exceed 72 hours");
        }
    }
}