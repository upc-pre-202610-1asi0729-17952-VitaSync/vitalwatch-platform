package com.vitalwatch.center.platform.clinicalrisk.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Resource used to create a clinical risk assessment.
 */
@Schema(name = "CreateClinicalRiskAssessmentRequest", description = "Request payload for creating a fatigue and risk assessment")
public record CreateClinicalRiskAssessmentResource(

        @NotNull
        @Positive
        @Schema(description = "Hospital workspace id", example = "1")
        Long hospitalWorkspaceId,

        @NotNull
        @Positive
        @Schema(description = "User account id", example = "1")
        Long userAccountId,

        @NotNull
        @Positive
        @Schema(description = "Vital sign reading id", example = "1")
        Long vitalSignReadingId
) {
}