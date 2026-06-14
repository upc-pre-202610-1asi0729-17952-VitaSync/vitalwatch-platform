package com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Resource used to start a staff recovery plan.
 */
@Schema(name = "StartRecoveryPlanRequest", description = "Request payload for starting a recovery plan")
public record StartRecoveryPlanResource(

        @NotNull
        @Positive
        @Schema(description = "User account id that starts the recovery plan", example = "1")
        Long startedByUserAccountId
) {
}