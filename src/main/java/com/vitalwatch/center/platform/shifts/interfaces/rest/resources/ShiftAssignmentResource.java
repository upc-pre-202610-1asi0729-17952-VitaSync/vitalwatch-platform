package com.vitalwatch.center.platform.shifts.interfaces.rest.resources;

import com.vitalwatch.center.platform.shifts.domain.model.enums.ShiftAssignmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Resource used to expose shift assignment data.
 */
@Schema(name = "ShiftAssignmentResponse", description = "Shift assignment information response")
public record ShiftAssignmentResource(

        @Schema(description = "Shift assignment id", example = "1")
        Long id,

        @Schema(description = "Work shift id", example = "1")
        Long workShiftId,

        @Schema(description = "Assigned user account id", example = "1")
        Long userAccountId,

        @Schema(description = "User account id that assigned the user", example = "1")
        Long assignedByUserAccountId,

        @Schema(description = "User account id that confirmed the assignment")
        Long confirmedByUserAccountId,

        @Schema(description = "User account id that released the assignment")
        Long releasedByUserAccountId,

        @Schema(description = "Assignment status", example = "ASSIGNED")
        ShiftAssignmentStatus status,

        @Schema(description = "Release reason")
        String releaseReason,

        @Schema(description = "Assignment date")
        Instant assignedAt,

        @Schema(description = "Confirmation date")
        Instant confirmedAt,

        @Schema(description = "Release date")
        Instant releasedAt
) {
}