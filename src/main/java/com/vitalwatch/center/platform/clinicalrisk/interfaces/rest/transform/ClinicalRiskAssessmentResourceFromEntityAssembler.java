package com.vitalwatch.center.platform.clinicalrisk.interfaces.rest.transform;

import com.vitalwatch.center.platform.clinicalrisk.domain.model.aggregates.ClinicalRiskAssessment;
import com.vitalwatch.center.platform.clinicalrisk.interfaces.rest.resources.ClinicalRiskAssessmentResource;

/**
 * Assembler to convert ClinicalRiskAssessment aggregate into ClinicalRiskAssessmentResource.
 */
public final class ClinicalRiskAssessmentResourceFromEntityAssembler {

    private ClinicalRiskAssessmentResourceFromEntityAssembler() {
    }

    public static ClinicalRiskAssessmentResource toResourceFromEntity(ClinicalRiskAssessment entity) {
        return new ClinicalRiskAssessmentResource(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getUserAccountId(),
                entity.getVitalSignReadingId(),
                entity.getFatigueScore(),
                entity.getRiskLevel(),
                entity.getStatus(),
                entity.getAssessedAt(),
                entity.getReviewedAt(),
                entity.getEscalatedAt(),
                entity.getClosedAt()
        );
    }
}