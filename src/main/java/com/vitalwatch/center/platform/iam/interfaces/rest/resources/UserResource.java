package com.vitalwatch.center.platform.iam.interfaces.rest.resources;

/**
 * Resource used to expose user data through the REST API.
 * Password hashes are never exposed.
 */
public record UserResource(
        Long id,
        String firstName,
        String lastName,
        String username,
        String email,
        String role,
        String status,
        Long organizationId,
        Long specialtyId,
        Long workAreaId
) {
}