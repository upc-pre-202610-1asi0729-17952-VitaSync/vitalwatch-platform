package com.vitalwatch.center.platform.iam.interfaces.rest.resources;

import com.vitalwatch.center.platform.iam.domain.model.enums.HospitalWorkspaceStatus;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resource used to expose hospital workspace data.
 */
@Schema(name = "HospitalWorkspaceResponse", description = "Hospital workspace information response")
public record HospitalWorkspaceResource(

        @Schema(description = "Hospital workspace unique identifier", example = "1")
        Long id,

        @Schema(description = "Hospital or clinic name", example = "Clinica San Gabriel")
        String name,

        @Schema(description = "Peruvian RUC", example = "20123456789")
        String ruc,

        @Schema(description = "Administrator profile id", example = "1")
        Long administratorProfileId,

        @Schema(description = "Administrator email", example = "admin@clinicasangabriel.com")
        String administratorEmail,

        @Schema(description = "Hospital workspace status", example = "ACTIVE")
        HospitalWorkspaceStatus status
) {
}