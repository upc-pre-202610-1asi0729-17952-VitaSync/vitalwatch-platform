package com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.resources;

import java.time.OffsetDateTime;

/**
 * Resource used to expose clinical alert data.
 */
public record ClinicalAlertResource(
        Long id,
        Long organizationId,
        Long userId,
        String severity,
        String status,
        String message,
        OffsetDateTime createdAt,
        OffsetDateTime resolvedAt,
        Long resolvedBy
) {
}