package com.vitalwatch.center.platform.profiles.domain.model.valueobjects;

/**
 * PersonName Value Object.
 */
public record PersonName(String firstName, String lastName) {

    public PersonName {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name must not be null or blank");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name must not be null or blank");
        }
    }

    public String getFullName() {
        return "%s %s".formatted(firstName, lastName);
    }
}