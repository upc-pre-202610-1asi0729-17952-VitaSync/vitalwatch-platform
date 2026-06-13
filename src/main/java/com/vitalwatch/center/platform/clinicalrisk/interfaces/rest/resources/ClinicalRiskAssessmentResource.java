package com.vitalwatch.center.platform.clinicalrisk.interfaces.rest.resources;

import com.vitalwatch.center.platform.clinicalrisk.domain.model.enums.AssessmentStatus;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.enums.RiskLevel;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Resource used to expose clinical risk assessment data.
 */
@Schema(name = "ClinicalRiskAssessmentResponse", description = "Fatigue and clinical risk assessment response")
public record ClinicalRiskAssessmentResource(

        @Schema(description = "Clinical risk assessment id", example = "1")
        Long id,

        @Schema(description = "Hospital workspace id", example = "1")
        Long hospitalWorkspaceId,

        @Schema(description = "User account id", example = "1")
        Long userAccountId,

        @Schema(description = "Vital sign reading id", example = "1")
        Long vitalSignReadingId,

        @Schema(description = "Fatigue score from 0 to 100", example = "68")
        Integer fatigueScore,

        @Schema(description = "Risk level", example = "HIGH")
        RiskLevel riskLevel,

        @Schema(description = "Assessment status", example = "CREATED")
        AssessmentStatus status,

        @Schema(description = "Assessment creation date")
        Instant assessedAt,

        @Schema(description = "Review date")
        Instant reviewedAt,

        @Schema(description = "Escalation date")
        Instant escalatedAt,

        @Schema(description = "Close date")
        Instant closedAt
) {
}