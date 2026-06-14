package com.vitalwatch.center.platform.staffrecovery.infrastructure.persistence.jpa.assemblers;

import com.vitalwatch.center.platform.staffrecovery.domain.model.aggregates.RecoveryPlan;
import com.vitalwatch.center.platform.staffrecovery.domain.model.valueobjects.RecoveryNotes;
import com.vitalwatch.center.platform.staffrecovery.domain.model.valueobjects.RestHours;
import com.vitalwatch.center.platform.staffrecovery.infrastructure.persistence.jpa.entities.RecoveryPlanPersistenceEntity;

/**
 * Assembler between RecoveryPlan domain and persistence representations.
 */
public final class RecoveryPlanPersistenceAssembler {

    private RecoveryPlanPersistenceAssembler() {
    }

    public static RecoveryPlan toDomainFromPersistence(RecoveryPlanPersistenceEntity entity) {
        return new RecoveryPlan(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getUserAccountId(),
                entity.getClinicalRiskAssessmentId(),
                entity.getIncidentId(),
                entity.getReason(),
                entity.getPriority(),
                entity.getStatus(),
                new RestHours(entity.getRecommendedRestHours()),
                new RecoveryNotes(entity.getNotes()),
                entity.getCompletionNotes(),
                entity.getCancellationReason(),
                entity.getStartedByUserAccountId(),
                entity.getCompletedByUserAccountId(),
                entity.getCancelledByUserAccountId(),
                entity.getPlanCreatedAt(),
                entity.getStartedAt(),
                entity.getCompletedAt(),
                entity.getCancelledAt()
        );
    }

    public static RecoveryPlanPersistenceEntity toPersistenceFromDomain(RecoveryPlan aggregate) {
        var entity = new RecoveryPlanPersistenceEntity();

        entity.setId(aggregate.getId());
        entity.setHospitalWorkspaceId(aggregate.getHospitalWorkspaceId());
        entity.setUserAccountId(aggregate.getUserAccountId());
        entity.setClinicalRiskAssessmentId(aggregate.getClinicalRiskAssessmentId());
        entity.setIncidentId(aggregate.getIncidentId());
        entity.setReason(aggregate.getReason());
        entity.setPriority(aggregate.getPriority());
        entity.setStatus(aggregate.getStatus());
        entity.setRecommendedRestHours(aggregate.getRecommendedRestHours());
        entity.setNotes(aggregate.getNotes());
        entity.setCompletionNotes(aggregate.getCompletionNotes());
        entity.setCancellationReason(aggregate.getCancellationReason());
        entity.setStartedByUserAccountId(aggregate.getStartedByUserAccountId());
        entity.setCompletedByUserAccountId(aggregate.getCompletedByUserAccountId());
        entity.setCancelledByUserAccountId(aggregate.getCancelledByUserAccountId());
        entity.setPlanCreatedAt(aggregate.getCreatedAt());
        entity.setStartedAt(aggregate.getStartedAt());
        entity.setCompletedAt(aggregate.getCompletedAt());
        entity.setCancelledAt(aggregate.getCancelledAt());

        return entity;
    }
}