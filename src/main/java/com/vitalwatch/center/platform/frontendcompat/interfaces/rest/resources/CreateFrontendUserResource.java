package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Frontend-compatible request for creating user accounts.
 */
@Schema(name = "CreateFrontendUserRequest", description = "User creation request compatible with the Angular frontend")
public record CreateFrontendUserResource(
        Long organizationId,
        Long hospitalWorkspaceId,
        String firstName,
        String lastName,
        String email,
        String password,
        String phone,
        Long workAreaId,
        Long specialtyId,
        String role,
        String status
) {
}