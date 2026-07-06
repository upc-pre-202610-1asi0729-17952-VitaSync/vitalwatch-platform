package com.vitalwatch.center.platform.iam.interfaces.rest.resources;

/**
 * Resource used to expose organization data through the REST API.
 */
public record OrganizationResource(
        Long id,
        String legalName,
        String commercialName,
        String ruc,
        String email,
        String phone,
        String address,
        String status
) {
}