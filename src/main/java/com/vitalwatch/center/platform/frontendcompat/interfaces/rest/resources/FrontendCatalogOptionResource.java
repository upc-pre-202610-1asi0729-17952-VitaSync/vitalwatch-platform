package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Frontend-compatible response for catalog options.
 */
@Schema(name = "FrontendCatalogOptionResponse", description = "Catalog option response compatible with the Angular frontend")
public record FrontendCatalogOptionResource(
        Long id,
        String code,
        String name,
        String label,
        String value,
        Boolean active
) {
}