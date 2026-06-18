package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Frontend-compatible request for recording compliance records.
 */
@Schema(name = "CreateFrontendComplianceRecordRequest", description = "Compliance record creation request compatible with the Angular frontend")
public record CreateFrontendComplianceRecordResource(
        Long organizationId,
        Long hospitalWorkspaceId,
        String resourceType,
        Long resourceId,
        String status,
        String description
) {
}