package com.vitalwatch.center.platform.incidents.interfaces.rest.resources;

import com.vitalwatch.center.platform.incidents.domain.model.enums.EscalationLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Resource used to escalate an incident.
 */
@Schema(name = "EscalateIncidentRequest", description = "Request payload for escalating an incident")
public record EscalateIncidentResource(

        @NotNull
        @Positive
        @Schema(description = "User account id that escalates the incident", example = "1")
        Long escalatedByUserAccountId,

        @NotNull
        @Schema(description = "Escalation level", example = "CLINICAL_SUPERVISOR")
        EscalationLevel escalationLevel
) {
}