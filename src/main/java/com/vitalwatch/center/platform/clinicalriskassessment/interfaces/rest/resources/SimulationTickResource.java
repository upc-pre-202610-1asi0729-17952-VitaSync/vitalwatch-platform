package com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.resources;

import java.time.OffsetDateTime;

/**
 * Resource used to expose the result of an IoT simulation tick.
 */
public record SimulationTickResource(
        Long organizationId,
        Integer doctorsProcessed,
        Integer readingsCreated,
        Integer riskAssessmentsUpdated,
        Integer alertsCreated,
        Integer anomaliesCreated,
        OffsetDateTime generatedAt
) {
}