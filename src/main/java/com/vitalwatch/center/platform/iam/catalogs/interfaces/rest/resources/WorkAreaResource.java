package com.vitalwatch.center.platform.iam.catalogs.interfaces.rest.resources;

/**
 * Resource used to expose medical work area data.
 */
public record WorkAreaResource(
        Long id,
        Long organizationId,
        String name
) {
}