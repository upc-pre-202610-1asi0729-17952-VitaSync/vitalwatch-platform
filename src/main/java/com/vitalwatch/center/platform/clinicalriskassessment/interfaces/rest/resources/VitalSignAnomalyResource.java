package com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.resources;

import java.time.OffsetDateTime;

/**
 * Resource used to expose vital sign anomaly data.
 */
public record VitalSignAnomalyResource(
        Long id,
        Long organizationId,
        Long userId,
        String type,
        String severity,
        String status,
        Double value,
        Double threshold,
        String message,
        OffsetDateTime detectedAt,
        OffsetDateTime reviewedAt,
        Long reviewedBy
) {
}