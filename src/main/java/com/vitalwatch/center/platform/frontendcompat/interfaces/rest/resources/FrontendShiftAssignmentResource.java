package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Frontend-compatible response for shift assignments.
 */
@Schema(name = "FrontendShiftAssignmentResponse", description = "Shift assignment response compatible with the Angular frontend")
public record FrontendShiftAssignmentResource(
        Long id,
        Long shiftRecordId,
        Long workShiftId,
        Long userAccountId,
        Long userId,
        Long assignedByUserAccountId,
        Long confirmedByUserAccountId,
        Long releasedByUserAccountId,
        String status,
        String releaseReason,
        Instant assignedAt,
        Instant confirmedAt,
        Instant releasedAt
) {
}