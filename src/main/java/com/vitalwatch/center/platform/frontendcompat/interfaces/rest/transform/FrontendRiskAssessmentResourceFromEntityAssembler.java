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
        var fatigueLevel = Math.max(1, Math.min(5, entity.getFatigueScore() / 20));
        var heartRate = 75 + fatigueLevel * 8;
        var hrv = Math.max(20, 90 - fatigueLevel * 8);

        return new FrontendRiskAssessmentResource(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getHospitalWorkspaceId(),
                entity.getUserAccountId(),
                entity.getUserAccountId(),
                entity.getVitalSignReadingId(),
                entity.getFatigueScore(),
                fatigueLevel,
                entity.getRiskLevel().name(),
                entity.getStatus().name(),
                heartRate,
                hrv,
                entity.getAssessedAt(),
                entity.getAssessedAt(),
                entity.getReviewedAt(),
                entity.getEscalatedAt(),
                entity.getClosedAt()
        );
    }
}