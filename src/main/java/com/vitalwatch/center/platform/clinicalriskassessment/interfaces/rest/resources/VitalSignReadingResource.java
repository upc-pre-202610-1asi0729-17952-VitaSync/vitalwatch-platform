package com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.resources;

import java.time.OffsetDateTime;

/**
 * Resource used to expose vital sign reading data.
 */
public record VitalSignReadingResource(
        Long id,
        Long organizationId,
        Long userId,
        Integer heartRate,
        Integer hrv,
        Integer fatigueLevel,
        Double cortisolLevel,
        String sensorStatus,
        OffsetDateTime recordedAt
) {
}