package com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources;

import java.time.OffsetDateTime;

/**
 * Resource used to expose preventive action data.
 */
public record PreventiveActionResource(
        Long id,
        Long organizationId,
        Long supervisorId,
        Long userId,
        String type,
        String status,
        String notes,
        OffsetDateTime createdAt,
        OffsetDateTime completedAt
) {
}