package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Frontend-compatible request for patching invitations.
 */
@Schema(name = "PatchFrontendInvitationRequest", description = "Invitation partial update request compatible with the Angular frontend")
public record PatchFrontendInvitationResource(
        String status
) {
}