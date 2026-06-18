package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Frontend-compatible request for creating a shift assignment.
 */
@Schema(name = "CreateFrontendShiftAssignmentRequest", description = "Shift assignment creation request compatible with the Angular frontend")
public record CreateFrontendShiftAssignmentResource(
        Long shiftRecordId,
        Long workShiftId,
        Long userAccountId,
        Long userId,
        Long assignedByUserAccountId
) {
}