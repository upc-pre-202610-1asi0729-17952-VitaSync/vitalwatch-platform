package com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.resources;

import com.vitalwatch.center.platform.clinicalriskassessment.domain.model.enums.VitalSignAnomalyStatus;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

/**
 * Resource used to update a vital sign anomaly status.
 */
public record UpdateVitalSignAnomalyStatusResource(
        @NotNull
        VitalSignAnomalyStatus status,

        OffsetDateTime reviewedAt,

        Long reviewedBy
) {
}