package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Frontend-compatible request for recovery plan lifecycle actions.
 */
@Schema(name = "FrontendRecoveryPlanActionRequest", description = "Recovery plan action request compatible with the Angular frontend")
public record FrontendRecoveryPlanActionResource(
        Long userAccountId,
        Long userId,
        Long startedByUserAccountId,
        Long completedByUserAccountId,
        Long cancelledByUserAccountId,
        String completionNotes,
        String cancellationReason,
        String notes,
        String reason
) {
}