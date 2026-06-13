package com.vitalwatch.center.platform.iam.domain.model.valueobjects;

/**
 * Hospital workspace name value object.
 */
public record HospitalWorkspaceName(String value) {

    public HospitalWorkspaceName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Hospital workspace name must not be null or blank");
        }
        if (value.length() < 3) {
            throw new IllegalArgumentException("Hospital workspace name must have at least 3 characters");
        }
    }
}