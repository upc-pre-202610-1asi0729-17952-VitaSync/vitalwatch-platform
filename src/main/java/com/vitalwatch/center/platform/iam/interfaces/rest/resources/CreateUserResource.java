package com.vitalwatch.center.platform.iam.interfaces.rest.resources;

import com.vitalwatch.center.platform.iam.domain.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Resource used to create a platform user.
 */
public record CreateUserResource(
        @NotBlank
        @Size(max = 80)
        String firstName,

        @NotBlank
        @Size(max = 80)
        String lastName,

        @NotBlank
        @Size(max = 80)
        String username,

        @NotBlank
        @Email
        @Size(max = 120)
        String email,

        @NotBlank
        @Size(min = 6, max = 80)
        String password,

        @NotNull
        UserRole role,

        Long organizationId,

        Long specialtyId,

        Long workAreaId
) {
}