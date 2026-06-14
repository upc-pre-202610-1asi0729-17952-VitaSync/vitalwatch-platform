package com.vitalwatch.center.platform.staffrecovery.interfaces.rest.transform;

import com.vitalwatch.center.platform.staffrecovery.domain.model.aggregates.RecoveryAction;
import com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources.RecoveryActionResource;

/**
 * Assembler to convert RecoveryAction aggregate into RecoveryActionResource.
 */
public final class RecoveryActionResourceFromEntityAssembler {

    private RecoveryActionResourceFromEntityAssembler() {
    }

    public static RecoveryActionResource toResourceFromEntity(RecoveryAction entity) {
        return new RecoveryActionResource(
                entity.getId(),
                entity.getRecoveryPlanId(),
                entity.getActionType(),
                entity.getNotes(),
                entity.getRecommendedRestHours(),
                entity.getCompleted(),
                entity.getCompletedByUserAccountId(),
                entity.getCreatedAt(),
                entity.getCompletedAt()
        );
    }
}