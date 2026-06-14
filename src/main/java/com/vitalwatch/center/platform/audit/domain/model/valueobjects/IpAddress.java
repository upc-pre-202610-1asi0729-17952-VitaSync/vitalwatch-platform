package com.vitalwatch.center.platform.audit.domain.model.valueobjects;

/**
 * IP address from where the auditable action was performed.
 */
public record IpAddress(String value) {

    public IpAddress {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("IP address must not be null or blank");
        }

        value = value.trim();

        if (value.length() > 100) {
            throw new IllegalArgumentException("IP address must not exceed 100 characters");
        }
    }
}