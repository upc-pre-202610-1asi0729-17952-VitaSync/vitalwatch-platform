package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Frontend-compatible response for audit logs.
 */
@Schema(name = "FrontendAuditLogResponse", description = "Audit log response compatible with the Angular frontend")
public record FrontendAuditLogResource(
        Long id,
        Long organizationId,
        Long hospitalWorkspaceId,
        Long actorUserAccountId,
        Long actorUserId,
        Long userId,
        String actionType,
        String type,
        String resourceType,
        Long resourceId,
        String severity,
        String description,
        String ipAddress,
        Instant occurredAt,
        Instant createdAt
) {
}