package com.vitalwatch.center.platform.staffrecovery.domain.model.valueobjects;

/**
 * Notes describing a recovery plan or recovery action.
 */
public record RecoveryNotes(String value) {

    public RecoveryNotes {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Recovery notes must not be null or blank");
        }

        value = value.trim();

        if (value.length() > 1000) {
            throw new IllegalArgumentException("Recovery notes must not exceed 1000 characters");
        }
    }
}