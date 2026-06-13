package com.vitalwatch.center.platform.incidents.interfaces.rest.resources;

import com.vitalwatch.center.platform.incidents.domain.model.enums.EscalationLevel;
import com.vitalwatch.center.platform.incidents.domain.model.enums.IncidentSeverity;
import com.vitalwatch.center.platform.incidents.domain.model.enums.IncidentSource;
import com.vitalwatch.center.platform.incidents.domain.model.enums.IncidentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Resource used to expose incident data.
 */
@Schema(name = "IncidentResponse", description = "Incident information response")
public record IncidentResource(

        @Schema(description = "Incident id", example = "1")
        Long id,

        @Schema(description = "Hospital workspace id", example = "1")
        Long hospitalWorkspaceId,

        @Schema(description = "Reported user account id", example = "1")
        Long reportedUserAccountId,

        @Schema(description = "Clinical risk assessment id", example = "2")
        Long clinicalRiskAssessmentId,

        @Schema(description = "Incident title")
        String title,

        @Schema(description = "Incident description")
        String description,

        @Schema(description = "Incident severity", example = "CRITICAL")
        IncidentSeverity severity,

        @Schema(description = "Incident source", example = "CLINICAL_RISK_ASSESSMENT")
        IncidentSource source,

        @Schema(description = "Incident status", example = "OPEN")
        IncidentStatus status,

        @Schema(description = "Escalation level", example = "NONE")
        EscalationLevel escalationLevel,

        @Schema(description = "User account id that acknowledged the incident")
        Long acknowledgedByUserAccountId,

        @Schema(description = "User account id that escalated the incident")
        Long escalatedByUserAccountId,

        @Schema(description = "User account id that resolved the incident")
        Long resolvedByUserAccountId,

        @Schema(description = "User account id that cancelled the incident")
        Long cancelledByUserAccountId,

        @Schema(description = "Resolution notes")
        String resolutionNotes,

        @Schema(description = "Cancellation reason")
        String cancellationReason,

        @Schema(description = "Incident creation date")
        Instant createdAt,

        @Schema(description = "Acknowledgement date")
        Instant acknowledgedAt,

        @Schema(description = "Escalation date")
        Instant escalatedAt,

        @Schema(description = "Resolution date")
        Instant resolvedAt,

        @Schema(description = "Cancellation date")
        Instant cancelledAt
) {
}