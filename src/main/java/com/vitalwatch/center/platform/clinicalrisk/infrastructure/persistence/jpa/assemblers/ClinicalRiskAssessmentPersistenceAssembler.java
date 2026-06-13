package com.vitalwatch.center.platform.clinicalrisk.infrastructure.persistence.jpa.assemblers;

import com.vitalwatch.center.platform.clinicalrisk.domain.model.aggregates.ClinicalRiskAssessment;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.valueobjects.FatigueScore;
import com.vitalwatch.center.platform.clinicalrisk.infrastructure.persistence.jpa.entities.ClinicalRiskAssessmentPersistenceEntity;

/**
 * Assembler between ClinicalRiskAssessment domain and persistence representations.
 */
public final class ClinicalRiskAssessmentPersistenceAssembler {

    private ClinicalRiskAssessmentPersistenceAssembler() {
    }

    public static ClinicalRiskAssessment toDomainFromPersistence(ClinicalRiskAssessmentPersistenceEntity entity) {
        return new ClinicalRiskAssessment(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getUserAccountId(),
                entity.getVitalSignReadingId(),
                new FatigueScore(entity.getFatigueScore()),
                entity.getRiskLevel(),
                entity.getStatus(),
                entity.getAssessedAt(),
                entity.getReviewedAt(),
                entity.getEscalatedAt(),
                entity.getClosedAt()
        );
    }

    public static ClinicalRiskAssessmentPersistenceEntity toPersistenceFromDomain(ClinicalRiskAssessment aggregate) {
        var entity = new ClinicalRiskAssessmentPersistenceEntity();
        entity.setId(aggregate.getId());
        entity.setHospitalWorkspaceId(aggregate.getHospitalWorkspaceId());
        entity.setUserAccountId(aggregate.getUserAccountId());
        entity.setVitalSignReadingId(aggregate.getVitalSignReadingId());
        entity.setFatigueScore(aggregate.getFatigueScore());
        entity.setRiskLevel(aggregate.getRiskLevel());
        entity.setStatus(aggregate.getStatus());
        entity.setAssessedAt(aggregate.getAssessedAt());
        entity.setReviewedAt(aggregate.getReviewedAt());
        entity.setEscalatedAt(aggregate.getEscalatedAt());
        entity.setClosedAt(aggregate.getClosedAt());
        return entity;
    }
}