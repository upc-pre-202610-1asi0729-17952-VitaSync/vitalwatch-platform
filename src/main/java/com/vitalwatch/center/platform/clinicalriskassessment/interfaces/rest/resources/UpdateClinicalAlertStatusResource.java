package com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.resources;

import com.vitalwatch.center.platform.clinicalriskassessment.domain.model.enums.ClinicalAlertStatus;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

/**
 * Resource used to update a clinical alert status.
 */
public record UpdateClinicalAlertStatusResource(
        @NotNull
        ClinicalAlertStatus status,

        OffsetDateTime resolvedAt,

        Long resolvedBy
) {
}