package com.vitalwatch.center.platform.clinicalrisk.domain.model.valueobjects;

/**
 * Quantity of hours used for sleep and shift workload measurements.
 */
public record HoursQuantity(Double value) {

    public HoursQuantity {
        if (value == null) {
            throw new IllegalArgumentException("Hours quantity must not be null");
        }
        if (value < 0 || value > 24) {
            throw new IllegalArgumentException("Hours quantity must be between 0 and 24");
        }
    }
}