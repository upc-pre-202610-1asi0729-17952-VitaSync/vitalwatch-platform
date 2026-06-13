package com.vitalwatch.center.platform.subscriptions.interfaces.rest.transform;

import com.vitalwatch.center.platform.subscriptions.domain.model.aggregates.HospitalSubscription;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources.HospitalSubscriptionResource;

/**
 * Assembler to convert HospitalSubscription aggregate into HospitalSubscriptionResource.
 */
public final class HospitalSubscriptionResourceFromEntityAssembler {

    private HospitalSubscriptionResourceFromEntityAssembler() {
    }

    public static HospitalSubscriptionResource toResourceFromEntity(HospitalSubscription entity) {
        return new HospitalSubscriptionResource(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getSubscriptionPlanId(),
                entity.getStatus(),
                entity.getStartedAt(),
                entity.getCancelledAt(),
                entity.getExpiresAt()
        );
    }
}