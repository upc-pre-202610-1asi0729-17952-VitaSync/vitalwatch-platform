package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Frontend-compatible response for hospital organizations.
 */
@Schema(name = "FrontendOrganizationResponse", description = "Organization response compatible with the Angular frontend")
public record FrontendOrganizationResource(
        Long id,
        String name,
        String ruc,
        String address,
        String phone,
        String status,
        Long planId
) {
}