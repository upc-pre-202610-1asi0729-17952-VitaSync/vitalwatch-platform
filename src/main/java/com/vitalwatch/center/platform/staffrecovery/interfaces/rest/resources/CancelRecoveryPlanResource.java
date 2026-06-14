package com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Resource used to cancel a staff recovery plan.
 */
@Schema(name = "CancelRecoveryPlanRequest", description = "Request payload for cancelling a recovery plan")
public record CancelRecoveryPlanResource(

        @NotNull
        @Positive
        @Schema(description = "User account id that cancels the recovery plan", example = "1")
        Long cancelledByUserAccountId,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Cancellation reason", example = "Recovery plan cancelled due to duplicated record.")
        String cancellationReason
) {
}