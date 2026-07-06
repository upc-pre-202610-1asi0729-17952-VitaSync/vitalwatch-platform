package com.vitalwatch.center.platform.iam.interfaces.rest.resources;

import com.vitalwatch.center.platform.iam.domain.model.enums.UserRole;
import com.vitalwatch.center.platform.iam.domain.model.enums.UserStatus;
import jakarta.validation.constraints.Size;

/**
 * Resource used to update user profile, role or status.
 * All fields are optional to support partial PATCH updates.
 */
public record UpdateUserResource(
        @Size(max = 80)
        String firstName,

        @Size(max = 80)
        String lastName,

        UserRole role,

        UserStatus status,

        Long specialtyId,

        Long workAreaId
) {
}