package com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources;

import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.RecoveryPlanReason;
import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.RecoveryPlanStatus;
import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.RecoveryPriority;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Resource used to expose recovery plan data.
 */
@Schema(name = "RecoveryPlanResponse", description = "Staff recovery plan information response")
public record RecoveryPlanResource(

        @Schema(description = "Recovery plan id", example = "1")
        Long id,

        @Schema(description = "Hospital workspace id", example = "1")
        Long hospitalWorkspaceId,

        @Schema(description = "User account id", example = "1")
        Long userAccountId,

        @Schema(description = "Related clinical risk assessment id", example = "2")
        Long clinicalRiskAssessmentId,

        @Schema(description = "Related incident id", example = "1")
        Long incidentId,

        @Schema(description = "Recovery plan reason", example = "CRITICAL_INCIDENT")
        RecoveryPlanReason reason,

        @Schema(description = "Recovery priority", example = "URGENT")
        RecoveryPriority priority,

        @Schema(description = "Recovery plan status", example = "CREATED")
        RecoveryPlanStatus status,

        @Schema(description = "Recommended rest hours", example = "12.0")
        Double recommendedRestHours,

        @Schema(description = "Recovery notes")
        String notes,

        @Schema(description = "Completion notes")
        String completionNotes,

        @Schema(description = "Cancellation reason")
        String cancellationReason,

        @Schema(description = "User account id that started the plan")
        Long startedByUserAccountId,

        @Schema(description = "User account id that completed the plan")
        Long completedByUserAccountId,

        @Schema(description = "User account id that cancelled the plan")
        Long cancelledByUserAccountId,

        @Schema(description = "Plan creation date")
        Instant createdAt,

        @Schema(description = "Plan start date")
        Instant startedAt,

        @Schema(description = "Plan completion date")
        Instant completedAt,

        @Schema(description = "Plan cancellation date")
        Instant cancelledAt
) {
}