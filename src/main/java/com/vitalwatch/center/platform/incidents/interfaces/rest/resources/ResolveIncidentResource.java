package com.vitalwatch.center.platform.incidents.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Resource used to resolve an incident.
 */
@Schema(name = "ResolveIncidentRequest", description = "Request payload for resolving an incident")
public record ResolveIncidentResource(

        @NotNull
        @Positive
        @Schema(description = "User account id that resolves the incident", example = "1")
        Long resolvedByUserAccountId,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Resolution notes", example = "Supervisor contacted the staff member and assigned immediate rest.")
        String resolutionNotes
) {
}