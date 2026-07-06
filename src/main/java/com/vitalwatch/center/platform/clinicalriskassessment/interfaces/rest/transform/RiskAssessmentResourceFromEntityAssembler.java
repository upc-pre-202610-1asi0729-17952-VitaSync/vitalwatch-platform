package com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.transform;

import com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.entities.RiskAssessmentJpaEntity;
import com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.resources.RiskAssessmentResource;

/**
 * Assembler used to convert risk assessment entities into REST resources.
 */
public final class RiskAssessmentResourceFromEntityAssembler {

    private RiskAssessmentResourceFromEntityAssembler() {
    }

    public static RiskAssessmentResource toResourceFromEntity(RiskAssessmentJpaEntity entity) {
        return new RiskAssessmentResource(
                entity.getId(),
                entity.getOrganizationId(),
                entity.getUserId(),
                entity.getFatigueLevel(),
                entity.getRiskLevel().name(),
                entity.getHeartRate(),
                entity.getHrv(),
                entity.getLastUpdatedAt()
        );
    }
}