package com.vitalwatch.center.platform.profiles.domain.model.events;

import com.vitalwatch.center.platform.profiles.domain.model.aggregates.Profile;

/**
 * Domain event published when a new Profile is created.
 */
public record ProfileCreatedEvent(
        Long profileId,
        String firstName,
        String lastName,
        String email,
        String street,
        String number,
        String city,
        String postalCode,
        String country
) {
    public static ProfileCreatedEvent from(Profile profile) {
        var name = profile.getName();
        var address = profile.getStreetAddressValue();

        return new ProfileCreatedEvent(
                profile.getId(),
                name.firstName(),
                name.lastName(),
                profile.getEmailAddress(),
                address.street(),
                address.number(),
                address.city(),
                address.postalCode(),
                address.country()
        );
    }
}