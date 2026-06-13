package com.vitalwatch.center.platform.profiles.interfaces.acl;

/**
 * ACL facade exposed by Profiles bounded context.
 */
public interface ProfilesContextFacade {

    Long createProfile(
            String firstName,
            String lastName,
            String email,
            String street,
            String number,
            String city,
            String postalCode,
            String country
    );

    Long fetchProfileIdByEmail(String email);
}