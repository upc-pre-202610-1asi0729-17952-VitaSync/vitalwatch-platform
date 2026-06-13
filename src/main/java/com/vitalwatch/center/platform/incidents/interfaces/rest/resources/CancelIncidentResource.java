package com.vitalwatch.center.platform.incidents.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Resource used to cancel an incident.
 */
@Schema(name = "CancelIncidentRequest", description = "Request payload for cancelling an incident")
public record CancelIncidentResource(

        @NotNull
        @Positive
        @Schema(description = "User account id that cancels the incident", example = "1")
        Long cancelledByUserAccountId,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Cancellation reason", example = "Incident created by mistake.")
        String cancellationReason
) {
}