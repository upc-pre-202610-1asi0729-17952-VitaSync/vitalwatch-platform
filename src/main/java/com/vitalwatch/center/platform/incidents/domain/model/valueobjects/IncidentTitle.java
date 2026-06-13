package com.vitalwatch.center.platform.incidents.domain.model.valueobjects;

/**
 * Short title that identifies an incident.
 */
public record IncidentTitle(String value) {

    public IncidentTitle {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Incident title must not be null or blank");
        }

        value = value.trim();

        if (value.length() > 120) {
            throw new IllegalArgumentException("Incident title must not exceed 120 characters");
        }
    }
}