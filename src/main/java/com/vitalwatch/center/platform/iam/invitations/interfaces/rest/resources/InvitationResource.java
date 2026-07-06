package com.vitalwatch.center.platform.iam.invitations.interfaces.rest.resources;

import java.time.LocalDateTime;

/**
 * Resource used to expose invitation data through the REST API.
 */
public record InvitationResource(
        Long id,
        String token,
        String email,
        String role,
        String status,
        Long organizationId,
        String organizationName,
        Long specialtyId,
        Long workAreaId,
        Long acceptedUserId,
        LocalDateTime expiresAt,
        LocalDateTime createdAt
) {
}