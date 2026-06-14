package com.vitalwatch.center.platform.shifts.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Resource used to confirm a shift assignment.
 */
@Schema(name = "ConfirmShiftAssignmentRequest", description = "Request payload for confirming a shift assignment")
public record ConfirmShiftAssignmentResource(

        @NotNull
        @Positive
        @Schema(description = "User account id that confirms the assignment", example = "1")
        Long confirmedByUserAccountId
) {
}