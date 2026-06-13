package com.vitalwatch.center.platform.iam.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Resource used to register a hospital workspace.
 */
@Schema(name = "CreateHospitalWorkspaceRequest", description = "Request payload for creating a hospital workspace")
public record CreateHospitalWorkspaceResource(

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Hospital or clinic name", example = "Clinica San Gabriel")
        String name,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Peruvian RUC with 11 digits", example = "20123456789")
        String ruc,

        @NotNull
        @Positive
        @Schema(description = "Profile id of the hospital administrator", example = "1")
        Long administratorProfileId,

        @NotBlank(message = "{validation.not-blank}")
        @Email(message = "{validation.email}")
        @Schema(description = "Hospital administrator email", example = "admin@clinicasangabriel.com")
        String administratorEmail
) {
}