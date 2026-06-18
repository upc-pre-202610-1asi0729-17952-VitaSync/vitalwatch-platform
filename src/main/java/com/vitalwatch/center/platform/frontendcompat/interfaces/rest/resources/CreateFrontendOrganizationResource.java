package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Frontend-compatible request for creating an organization.
 */
@Schema(name = "CreateFrontendOrganizationRequest", description = "Organization creation request compatible with the Angular frontend")
public record CreateFrontendOrganizationResource(
        @NotBlank(message = "{validation.not-blank}")
        String name,

        @NotBlank(message = "{validation.not-blank}")
        String ruc,

        @NotBlank(message = "{validation.not-blank}")
        String address,

        @NotBlank(message = "{validation.not-blank}")
        String phone,

        @NotNull
        @Positive
        Long planId
) {
}