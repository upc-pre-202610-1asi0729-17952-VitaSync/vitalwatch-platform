package com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.transform;

import com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.entities.ClinicalAlertJpaEntity;
import com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.resources.ClinicalAlertResource;

/**
 * Assembler used to convert clinical alert entities into REST resources.
 */
public final class ClinicalAlertResourceFromEntityAssembler {

    private ClinicalAlertResourceFromEntityAssembler() {
    }

    public static ClinicalAlertResource toResourceFromEntity(ClinicalAlertJpaEntity entity) {
        return new ClinicalAlertResource(
                entity.getId(),
                entity.getOrganizationId(),
                entity.getUserId(),
                entity.getSeverity().name(),
                entity.getStatus().name(),
                entity.getMessage(),
                entity.getCreatedAt(),
                entity.getResolvedAt(),
                entity.getResolvedBy()
        );
    }
}