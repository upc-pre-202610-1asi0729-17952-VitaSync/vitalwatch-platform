package com.vitalwatch.center.platform.iam.domain.model.valueobjects;

import java.util.regex.Pattern;

/**
 * Email address value object for IAM.
 */
public record EmailAddress(String value) {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public EmailAddress {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email address must not be null or blank");
        }
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Email address must be valid");
        }
    }
}