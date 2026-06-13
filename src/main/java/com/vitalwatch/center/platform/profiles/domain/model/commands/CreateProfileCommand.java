package com.vitalwatch.center.platform.profiles.domain.model.commands;

/**
 * Command used to create a new profile.
 */
public record CreateProfileCommand(
        String firstName,
        String lastName,
        String email,
        String street,
        String number,
        String city,
        String postalCode,
        String country
) {
}