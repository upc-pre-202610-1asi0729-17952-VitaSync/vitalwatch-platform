package com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.assemblers;

import com.vitalwatch.center.platform.subscriptions.domain.model.aggregates.HospitalSubscription;
import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.entities.HospitalSubscriptionPersistenceEntity;

/**
 * Assembler between HospitalSubscription domain and persistence representations.
 */
public final class HospitalSubscriptionPersistenceAssembler {

    private HospitalSubscriptionPersistenceAssembler() {
    }

    public static HospitalSubscription toDomainFromPersistence(HospitalSubscriptionPersistenceEntity entity) {
        return new HospitalSubscription(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getSubscriptionPlanId(),
                entity.getStatus(),
                entity.getStartedAt(),
                entity.getCancelledAt(),
                entity.getExpiresAt()
        );
    }

    public static HospitalSubscriptionPersistenceEntity toPersistenceFromDomain(HospitalSubscription aggregate) {
        var entity = new HospitalSubscriptionPersistenceEntity();
        entity.setId(aggregate.getId());
        entity.setHospitalWorkspaceId(aggregate.getHospitalWorkspaceId());
        entity.setSubscriptionPlanId(aggregate.getSubscriptionPlanId());
        entity.setStatus(aggregate.getStatus());
        entity.setStartedAt(aggregate.getStartedAt());
        entity.setCancelledAt(aggregate.getCancelledAt());
        entity.setExpiresAt(aggregate.getExpiresAt());
        return entity;
    }
}