package com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.resources;

import java.time.OffsetDateTime;

/**
 * Resource used to expose risk assessment data.
 */
public record RiskAssessmentResource(
        Long id,
        Long organizationId,
        Long userId,
        Integer fatigueLevel,
        String riskLevel,
        Integer heartRate,
        Integer hrv,
        OffsetDateTime lastUpdatedAt
) {
}