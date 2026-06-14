package com.vitalwatch.center.platform.audit.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Resource used to review a compliance record.
 */
@Schema(name = "ReviewComplianceRecordRequest", description = "Request payload for reviewing a compliance record")
public record ReviewComplianceRecordResource(

        @NotNull
        @Positive
        @Schema(description = "User account id that reviews the record", example = "1")
        Long reviewedByUserAccountId,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Review notes", example = "The recovery plan was reviewed and complies with the expected fatigue response protocol.")
        String reviewNotes
) {
}