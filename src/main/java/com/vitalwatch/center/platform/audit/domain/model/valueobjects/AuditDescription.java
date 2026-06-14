package com.vitalwatch.center.platform.audit.domain.model.valueobjects;

/**
 * Description of an audit event.
 */
public record AuditDescription(String value) {

    public AuditDescription {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Audit description must not be null or blank");
        }

        value = value.trim();

        if (value.length() > 1000) {
            throw new IllegalArgumentException("Audit description must not exceed 1000 characters");
        }
    }
}