package com.vitalwatch.center.platform.incidents.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Resource used to acknowledge an incident.
 */
@Schema(name = "AcknowledgeIncidentRequest", description = "Request payload for acknowledging an incident")
public record AcknowledgeIncidentResource(

        @NotNull
        @Positive
        @Schema(description = "User account id that acknowledges the incident", example = "1")
        Long acknowledgedByUserAccountId
) {
}