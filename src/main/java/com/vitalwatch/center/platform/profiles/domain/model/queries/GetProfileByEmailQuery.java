package com.vitalwatch.center.platform.profiles.domain.model.queries;

import com.vitalwatch.center.platform.profiles.domain.model.valueobjects.EmailAddress;

/**
 * Query to get a profile by email.
 */
public record GetProfileByEmailQuery(EmailAddress emailAddress) {
}