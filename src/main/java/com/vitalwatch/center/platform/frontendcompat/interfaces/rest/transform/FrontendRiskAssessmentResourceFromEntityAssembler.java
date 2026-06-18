package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.transform;

import com.vitalwatch.center.platform.clinicalrisk.domain.model.aggregates.ClinicalRiskAssessment;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendRiskAssessmentResource;

/**
 * Assembler to expose ClinicalRiskAssessment using the contract expected by the Angular frontend.
 */
public final class FrontendRiskAssessmentResourceFromEntityAssembler {

    private FrontendRiskAssessmentResourceFromEntityAssembler() {
    }

    public static FrontendRiskAssessmentResource toResourceFromEntity(ClinicalRiskAssessment entity) {
        return new FrontendRiskAssessmentResource(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getHospitalWorkspaceId(),
                entity.getUserAccountId(),
                entity.getUserAccountId(),
                entity.getVitalSignReadingId(),
                entity.getFatigueScore(),
                entity.getRiskLevel().name(),
                entity.getStatus().name(),
                entity.getAssessedAt(),
                entity.getReviewedAt(),
                entity.getEscalatedAt(),
                entity.getClosedAt()
        );
    }
}