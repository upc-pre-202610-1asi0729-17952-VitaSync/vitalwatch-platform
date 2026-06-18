package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Frontend-compatible request for partially updating user accounts.
 */
@Schema(name = "PatchFrontendUserRequest", description = "User partial update request compatible with the Angular frontend")
public record PatchFrontendUserResource(
        String firstName,
        String lastName,
        String email,
        String phone,
        Long workAreaId,
        Long specialtyId,
        String role,
        String status
) {
}