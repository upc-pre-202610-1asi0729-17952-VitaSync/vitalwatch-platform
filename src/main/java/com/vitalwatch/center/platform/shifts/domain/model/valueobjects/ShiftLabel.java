package com.vitalwatch.center.platform.shifts.domain.model.valueobjects;

/**
 * Short label that identifies a work shift.
 */
public record ShiftLabel(String value) {

    public ShiftLabel {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Shift label must not be null or blank");
        }

        value = value.trim();

        if (value.length() > 120) {
            throw new IllegalArgumentException("Shift label must not exceed 120 characters");
        }
    }
}