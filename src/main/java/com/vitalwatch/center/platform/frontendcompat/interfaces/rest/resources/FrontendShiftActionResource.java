package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Frontend-compatible request for simple shift actions.
 */
@Schema(name = "FrontendShiftActionRequest", description = "Shift action request compatible with the Angular frontend")
public record FrontendShiftActionResource(
        Long userAccountId,
        Long userId,
        String reason,
        String cancellationReason,
        String releaseReason
) {
}