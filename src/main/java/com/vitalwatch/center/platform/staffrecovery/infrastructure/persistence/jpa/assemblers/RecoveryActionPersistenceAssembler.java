package com.vitalwatch.center.platform.staffrecovery.infrastructure.persistence.jpa.assemblers;

import com.vitalwatch.center.platform.staffrecovery.domain.model.aggregates.RecoveryAction;
import com.vitalwatch.center.platform.staffrecovery.domain.model.valueobjects.RecoveryNotes;
import com.vitalwatch.center.platform.staffrecovery.domain.model.valueobjects.RestHours;
import com.vitalwatch.center.platform.staffrecovery.infrastructure.persistence.jpa.entities.RecoveryActionPersistenceEntity;

/**
 * Assembler between RecoveryAction domain and persistence representations.
 */
public final class RecoveryActionPersistenceAssembler {

    private RecoveryActionPersistenceAssembler() {
    }

    public static RecoveryAction toDomainFromPersistence(RecoveryActionPersistenceEntity entity) {
        return new RecoveryAction(
                entity.getId(),
                entity.getRecoveryPlanId(),
                entity.getActionType(),
                new RecoveryNotes(entity.getNotes()),
                new RestHours(entity.getRecommendedRestHours()),
                entity.getCompleted(),
                entity.getCompletedByUserAccountId(),
                entity.getActionCreatedAt(),
                entity.getCompletedAt()
        );
    }

    public static RecoveryActionPersistenceEntity toPersistenceFromDomain(RecoveryAction aggregate) {
        var entity = new RecoveryActionPersistenceEntity();

        entity.setId(aggregate.getId());
        entity.setRecoveryPlanId(aggregate.getRecoveryPlanId());
        entity.setActionType(aggregate.getActionType());
        entity.setNotes(aggregate.getNotes());
        entity.setRecommendedRestHours(aggregate.getRecommendedRestHours());
        entity.setCompleted(aggregate.getCompleted());
        entity.setCompletedByUserAccountId(aggregate.getCompletedByUserAccountId());
        entity.setActionCreatedAt(aggregate.getCreatedAt());
        entity.setCompletedAt(aggregate.getCompletedAt());

        return entity;
    }
}