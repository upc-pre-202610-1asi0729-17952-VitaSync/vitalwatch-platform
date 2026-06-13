package com.vitalwatch.center.platform.incidents.domain.model.valueobjects;

/**
 * Detailed description of an incident.
 */
public record IncidentDescription(String value) {

    public IncidentDescription {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Incident description must not be null or blank");
        }

        value = value.trim();

        if (value.length() > 1000) {
            throw new IllegalArgumentException("Incident description must not exceed 1000 characters");
        }
    }
}