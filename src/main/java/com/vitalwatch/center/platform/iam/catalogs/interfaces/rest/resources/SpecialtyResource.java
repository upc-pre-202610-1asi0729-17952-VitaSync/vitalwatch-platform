package com.vitalwatch.center.platform.iam.catalogs.interfaces.rest.resources;

/**
 * Resource used to expose medical specialty data.
 */
public record SpecialtyResource(
        Long id,
        String name
) {
}