package com.vitalwatch.center.platform.shifts.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Resource used to assign a user to a work shift.
 */
@Schema(name = "AssignUserToShiftRequest", description = "Request payload for assigning a user to a work shift")
public record AssignUserToShiftResource(

        @NotNull
        @Positive
        @Schema(description = "Work shift id", example = "1")
        Long workShiftId,

        @NotNull
        @Positive
        @Schema(description = "Assigned user account id", example = "1")
        Long userAccountId,

        @NotNull
        @Positive
        @Schema(description = "User account id that creates the assignment", example = "1")
        Long assignedByUserAccountId
) {
}