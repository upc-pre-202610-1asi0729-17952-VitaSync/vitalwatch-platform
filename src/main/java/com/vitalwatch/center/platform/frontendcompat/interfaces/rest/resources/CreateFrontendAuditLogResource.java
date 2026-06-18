package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Frontend-compatible request for recording audit logs.
 */
@Schema(name = "CreateFrontendAuditLogRequest", description = "Audit log creation request compatible with the Angular frontend")
public record CreateFrontendAuditLogResource(
        Long organizationId,
        Long hospitalWorkspaceId,
        Long actorUserAccountId,
        Long userId,
        String actionType,
        String resourceType,
        Long resourceId,
        String severity,
        String description,
        String ipAddress
) {
}