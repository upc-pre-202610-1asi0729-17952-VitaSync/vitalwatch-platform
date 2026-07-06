package com.vitalwatch.center.platform.subscriptions.interfaces.rest.transform;

import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.entities.CheckoutSessionJpaEntity;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources.CheckoutSessionResource;

/**
 * Assembler used to convert checkout session entities into REST resources.
 */
public final class CheckoutSessionResourceFromEntityAssembler {

    private CheckoutSessionResourceFromEntityAssembler() {
    }

    public static CheckoutSessionResource toResourceFromEntity(CheckoutSessionJpaEntity entity) {
        var organizationId = entity.getOrganization() == null
                ? null
                : entity.getOrganization().getId();

        return new CheckoutSessionResource(
                entity.getId(),
                entity.getSessionId(),
                entity.getSessionId(),
                entity.getCheckoutUrl(),
                entity.getStatus().name(),
                organizationId,
                entity.getPlan().getId(),
                entity.getPlan().getCode(),
                entity.getPlan().getName(),
                entity.getPlan().getPrice(),
                entity.getPlan().getCurrency(),
                entity.getPlan().getBillingPeriod()
        );
    }
}