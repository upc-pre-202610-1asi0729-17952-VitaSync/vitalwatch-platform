package com.vitalwatch.center.platform.shifts.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Resource used to complete a work shift.
 */
@Schema(name = "CompleteWorkShiftRequest", description = "Request payload for completing a work shift")
public record CompleteWorkShiftResource(

        @NotNull
        @Positive
        @Schema(description = "User account id that completes the shift", example = "1")
        Long completedByUserAccountId
) {
}