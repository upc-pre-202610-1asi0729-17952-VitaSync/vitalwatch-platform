package com.vitalwatch.center.platform.subscriptions.interfaces.rest.transform;

import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.entities.SubscriptionJpaEntity;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources.SubscriptionResource;

/**
 * Assembler used to convert subscription entities into REST resources.
 */
public final class SubscriptionResourceFromEntityAssembler {

    private SubscriptionResourceFromEntityAssembler() {
    }

    public static SubscriptionResource toResourceFromEntity(SubscriptionJpaEntity entity) {
        return new SubscriptionResource(
                entity.getId(),
                entity.getOrganization().getId(),
                entity.getOrganization().getCommercialName(),
                entity.getPlan().getId(),
                entity.getPlan().getCode(),
                entity.getPlan().getName(),
                entity.getStatus().name(),
                entity.getStartedAt(),
                entity.getExpiresAt(),
                entity.getStripeCustomerId(),
                entity.getStripeSubscriptionId()
        );
    }
}