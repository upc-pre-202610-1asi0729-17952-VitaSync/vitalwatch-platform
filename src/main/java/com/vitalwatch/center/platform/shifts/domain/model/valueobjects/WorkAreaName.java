package com.vitalwatch.center.platform.shifts.domain.model.valueobjects;

/**
 * Hospital work area name.
 */
public record WorkAreaName(String value) {

    public WorkAreaName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Work area name must not be null or blank");
        }

        value = value.trim();

        if (value.length() > 120) {
            throw new IllegalArgumentException("Work area name must not exceed 120 characters");
        }
    }
}