package com.vitalwatch.center.platform.iam.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Resource used to authenticate a platform user.
 */
public record SignInResource(
        @NotBlank
        @Email
        String email,

        @NotBlank
        String password
) {
}