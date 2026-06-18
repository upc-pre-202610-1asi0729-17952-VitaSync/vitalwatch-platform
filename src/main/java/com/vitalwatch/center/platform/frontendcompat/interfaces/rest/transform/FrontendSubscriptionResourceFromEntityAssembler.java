package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.transform;

import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendSubscriptionResource;
import com.vitalwatch.center.platform.subscriptions.domain.model.aggregates.HospitalSubscription;

/**
 * Assembler to expose HospitalSubscription using the contract expected by the Angular frontend.
 */
public final class FrontendSubscriptionResourceFromEntityAssembler {

    private FrontendSubscriptionResourceFromEntityAssembler() {
    }

    public static FrontendSubscriptionResource toResourceFromEntity(HospitalSubscription entity) {
        return new FrontendSubscriptionResource(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getHospitalWorkspaceId(),
                entity.getSubscriptionPlanId(),
                entity.getSubscriptionPlanId(),
                entity.getStatus().name(),
                entity.getStartedAt(),
                entity.getStartedAt(),
                entity.getCancelledAt(),
                entity.getExpiresAt()
        );
    }
}