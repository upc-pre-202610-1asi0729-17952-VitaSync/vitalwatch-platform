package com.vitalwatch.center.platform.incidents.interfaces.rest.resources;

import com.vitalwatch.center.platform.incidents.domain.model.enums.IncidentSeverity;
import com.vitalwatch.center.platform.incidents.domain.model.enums.IncidentSource;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Resource used to create an incident.
 */
@Schema(name = "CreateIncidentRequest", description = "Request payload for creating an incident")
public record CreateIncidentResource(

        @NotNull
        @Positive
        @Schema(description = "Hospital workspace id", example = "1")
        Long hospitalWorkspaceId,

        @NotNull
        @Positive
        @Schema(description = "Reported user account id", example = "1")
        Long reportedUserAccountId,

        @Schema(description = "Clinical risk assessment id related to the incident", example = "2")
        Long clinicalRiskAssessmentId,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Incident title", example = "Critical fatigue risk detected")
        String title,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Incident description", example = "The staff member shows critical fatigue indicators and requires immediate supervision.")
        String description,

        @NotNull
        @Schema(description = "Incident severity", example = "CRITICAL")
        IncidentSeverity severity,

        @NotNull
        @Schema(description = "Incident source", example = "CLINICAL_RISK_ASSESSMENT")
        IncidentSource source
) {
}