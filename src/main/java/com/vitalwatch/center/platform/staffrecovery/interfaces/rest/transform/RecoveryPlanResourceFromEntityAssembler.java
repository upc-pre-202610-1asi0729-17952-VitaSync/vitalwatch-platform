package com.vitalwatch.center.platform.staffrecovery.interfaces.rest.transform;

import com.vitalwatch.center.platform.staffrecovery.domain.model.aggregates.RecoveryPlan;
import com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources.RecoveryPlanResource;

/**
 * Assembler to convert RecoveryPlan aggregate into RecoveryPlanResource.
 */
public final class RecoveryPlanResourceFromEntityAssembler {

    private RecoveryPlanResourceFromEntityAssembler() {
    }

    public static RecoveryPlanResource toResourceFromEntity(RecoveryPlan entity) {
        return new RecoveryPlanResource(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getUserAccountId(),
                entity.getClinicalRiskAssessmentId(),
                entity.getIncidentId(),
                entity.getReason(),
                entity.getPriority(),
                entity.getStatus(),
                entity.getRecommendedRestHours(),
                entity.getNotes(),
                entity.getCompletionNotes(),
                entity.getCancellationReason(),
                entity.getStartedByUserAccountId(),
                entity.getCompletedByUserAccountId(),
                entity.getCancelledByUserAccountId(),
                entity.getCreatedAt(),
                entity.getStartedAt(),
                entity.getCompletedAt(),
                entity.getCancelledAt()
        );
    }
}