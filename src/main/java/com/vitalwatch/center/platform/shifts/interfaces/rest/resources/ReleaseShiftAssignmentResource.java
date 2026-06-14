package com.vitalwatch.center.platform.shifts.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Resource used to release a shift assignment.
 */
@Schema(name = "ReleaseShiftAssignmentRequest", description = "Request payload for releasing a shift assignment")
public record ReleaseShiftAssignmentResource(

        @NotNull
        @Positive
        @Schema(description = "User account id that releases the assignment", example = "1")
        Long releasedByUserAccountId,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Release reason", example = "Staff member requires recovery after fatigue alert.")
        String releaseReason
) {
}