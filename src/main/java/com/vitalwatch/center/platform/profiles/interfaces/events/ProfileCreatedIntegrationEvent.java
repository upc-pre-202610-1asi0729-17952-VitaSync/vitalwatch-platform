package com.vitalwatch.center.platform.profiles.interfaces.events;

import com.vitalwatch.center.platform.profiles.domain.model.aggregates.Profile;

/**
 * Integration event published by Profiles bounded context.
 */
public record ProfileCreatedIntegrationEvent(
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
    public static ProfileCreatedIntegrationEvent from(Profile profile) {
        var name = profile.getName();
        var address = profile.getStreetAddressValue();

        return new ProfileCreatedIntegrationEvent(
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