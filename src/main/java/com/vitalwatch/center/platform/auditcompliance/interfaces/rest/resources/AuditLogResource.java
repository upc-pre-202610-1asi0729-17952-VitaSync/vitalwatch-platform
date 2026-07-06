package com.vitalwatch.center.platform.auditcompliance.interfaces.rest.resources;

import java.time.OffsetDateTime;

/**
 * Resource used to expose audit log data.
 */
public record AuditLogResource(
        Long id,
        Long organizationId,
        Long actorUserId,
        String type,
        String severity,
        String resourceType,
        Long resourceId,
        String description,
        OffsetDateTime createdAt
) {
}