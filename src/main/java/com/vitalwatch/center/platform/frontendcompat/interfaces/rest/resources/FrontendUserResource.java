package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Frontend-compatible response for user accounts.
 */
@Schema(name = "FrontendUserResponse", description = "User response compatible with the Angular frontend")
public record FrontendUserResource(
        Long id,
        Long organizationId,
        Long hospitalWorkspaceId,
        Long profileId,
        String email,
        String name,
        String role,
        String status
) {
}