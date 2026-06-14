package com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources;

import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.RecoveryPlanReason;
import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.RecoveryPriority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Resource used to create a staff recovery plan.
 */
@Schema(name = "CreateRecoveryPlanRequest", description = "Request payload for creating a staff recovery plan")
public record CreateRecoveryPlanResource(

        @NotNull
        @Positive
        @Schema(description = "Hospital workspace id", example = "1")
        Long hospitalWorkspaceId,

        @NotNull
        @Positive
        @Schema(description = "User account id that requires recovery", example = "1")
        Long userAccountId,

        @Positive
        @Schema(description = "Related clinical risk assessment id", example = "2")
        Long clinicalRiskAssessmentId,

        @Positive
        @Schema(description = "Related incident id", example = "1")
        Long incidentId,

        @NotNull
        @Schema(description = "Reason that originated the recovery plan", example = "CRITICAL_INCIDENT")
        RecoveryPlanReason reason,

        @NotNull
        @Schema(description = "Recovery priority", example = "URGENT")
        RecoveryPriority priority,

        @NotNull
        @PositiveOrZero
        @Schema(description = "Recommended rest hours", example = "12.0")
        Double recommendedRestHours,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Recovery notes", example = "Staff member should be released from active duties and monitored by supervisor.")
        String notes
) {
}