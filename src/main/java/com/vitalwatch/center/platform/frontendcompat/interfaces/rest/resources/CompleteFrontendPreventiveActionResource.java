package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Frontend-compatible request for completing a preventive action.
 */
@Schema(name = "CompleteFrontendPreventiveActionRequest", description = "Preventive action completion request compatible with the Angular frontend")
public record CompleteFrontendPreventiveActionResource(
        Long userAccountId,
        Long userId,
        Long completedByUserAccountId
) {
}