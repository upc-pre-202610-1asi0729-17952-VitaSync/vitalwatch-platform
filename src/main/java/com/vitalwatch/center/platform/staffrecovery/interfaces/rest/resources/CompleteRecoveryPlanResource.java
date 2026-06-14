package com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Resource used to complete a staff recovery plan.
 */
@Schema(name = "CompleteRecoveryPlanRequest", description = "Request payload for completing a recovery plan")
public record CompleteRecoveryPlanResource(

        @NotNull
        @Positive
        @Schema(description = "User account id that completes the recovery plan", example = "1")
        Long completedByUserAccountId,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Completion notes", example = "Staff member completed rest period and was cleared for reduced workload.")
        String completionNotes
) {
}