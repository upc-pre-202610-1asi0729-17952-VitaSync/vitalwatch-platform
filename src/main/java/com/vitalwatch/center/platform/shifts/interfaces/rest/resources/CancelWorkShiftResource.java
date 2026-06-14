package com.vitalwatch.center.platform.shifts.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Resource used to cancel a work shift.
 */
@Schema(name = "CancelWorkShiftRequest", description = "Request payload for cancelling a work shift")
public record CancelWorkShiftResource(

        @NotNull
        @Positive
        @Schema(description = "User account id that cancels the shift", example = "1")
        Long cancelledByUserAccountId,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Cancellation reason", example = "Shift cancelled due to operational rescheduling.")
        String cancellationReason
) {
}