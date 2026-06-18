package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Frontend-compatible request for patching preventive actions.
 */
@Schema(name = "PatchFrontendPreventiveActionRequest", description = "Preventive action partial update request compatible with Angular")
public record PatchFrontendPreventiveActionResource(
        String status,
        Boolean completed,
        Long userAccountId,
        Long userId,
        Long completedByUserAccountId,
        String notes
) {
}