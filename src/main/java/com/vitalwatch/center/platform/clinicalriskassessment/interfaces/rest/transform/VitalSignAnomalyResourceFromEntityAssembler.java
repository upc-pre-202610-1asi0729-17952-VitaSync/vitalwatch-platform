package com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.transform;

import com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.entities.VitalSignAnomalyJpaEntity;
import com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.resources.VitalSignAnomalyResource;

/**
 * Assembler used to convert vital sign anomaly entities into REST resources.
 */
public final class VitalSignAnomalyResourceFromEntityAssembler {

    private VitalSignAnomalyResourceFromEntityAssembler() {
    }

    public static VitalSignAnomalyResource toResourceFromEntity(VitalSignAnomalyJpaEntity entity) {
        return new VitalSignAnomalyResource(
                entity.getId(),
                entity.getOrganizationId(),
                entity.getUserId(),
                entity.getType().name(),
                entity.getSeverity().name(),
                entity.getStatus().name(),
                entity.getValue(),
                entity.getThreshold(),
                entity.getMessage(),
                entity.getDetectedAt(),
                entity.getReviewedAt(),
                entity.getReviewedBy()
        );
    }
}