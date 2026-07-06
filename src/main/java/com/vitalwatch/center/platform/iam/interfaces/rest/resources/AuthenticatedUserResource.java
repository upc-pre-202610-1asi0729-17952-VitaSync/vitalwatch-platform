package com.vitalwatch.center.platform.iam.interfaces.rest.resources;

/**
 * Resource returned after a successful sign-in.
 */
public record AuthenticatedUserResource(
        Long id,
        String firstName,
        String lastName,
        String username,
        String email,
        String role,
        String status,
        Long organizationId,
        String token
) {
}