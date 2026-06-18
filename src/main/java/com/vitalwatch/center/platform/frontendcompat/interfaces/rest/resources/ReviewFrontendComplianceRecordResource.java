package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Frontend-compatible request for reviewing compliance records.
 */
@Schema(name = "ReviewFrontendComplianceRecordRequest", description = "Compliance record review request compatible with the Angular frontend")
public record ReviewFrontendComplianceRecordResource(
        Long reviewedByUserAccountId,
        Long reviewedByUserId,
        Long userAccountId,
        Long userId,
        String reviewNotes,
        String notes
) {
}