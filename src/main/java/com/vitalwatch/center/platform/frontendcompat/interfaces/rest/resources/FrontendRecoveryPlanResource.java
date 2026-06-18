package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Frontend-compatible response for recovery plans.
 */
@Schema(name = "FrontendRecoveryPlanResponse", description = "Recovery plan response compatible with the Angular frontend")
public record FrontendRecoveryPlanResource(
        Long id,
        Long organizationId,
        Long hospitalWorkspaceId,
        Long userAccountId,
        Long userId,
        Long clinicalRiskAssessmentId,
        Long incidentId,
        String reason,
        String priority,
        String status,
        Double recommendedRestHours,
        String notes,
        String completionNotes,
        String cancellationReason,
        Long startedByUserAccountId,
        Long completedByUserAccountId,
        Long cancelledByUserAccountId,
        Instant createdAt,
        Instant startedAt,
        Instant completedAt,
        Instant cancelledAt
) {
}