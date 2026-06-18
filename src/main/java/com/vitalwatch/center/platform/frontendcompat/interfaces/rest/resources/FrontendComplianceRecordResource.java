package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Frontend-compatible response for compliance records.
 */
@Schema(name = "FrontendComplianceRecordResponse", description = "Compliance record response compatible with the Angular frontend")
public record FrontendComplianceRecordResource(
        Long id,
        Long organizationId,
        Long hospitalWorkspaceId,
        String resourceType,
        Long resourceId,
        String status,
        String description,
        Long reviewedByUserAccountId,
        Long reviewedByUserId,
        String reviewNotes,
        Instant recordedAt,
        Instant createdAt,
        Instant reviewedAt
) {
}