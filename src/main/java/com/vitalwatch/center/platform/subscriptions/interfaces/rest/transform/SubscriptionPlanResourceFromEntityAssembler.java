package com.vitalwatch.center.platform.subscriptions.interfaces.rest.transform;

import com.vitalwatch.center.platform.subscriptions.domain.model.aggregates.SubscriptionPlan;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources.SubscriptionPlanResource;

/**
 * Assembler to convert SubscriptionPlan aggregate into SubscriptionPlanResource.
 */
public final class SubscriptionPlanResourceFromEntityAssembler {

    private SubscriptionPlanResourceFromEntityAssembler() {
    }

    public static SubscriptionPlanResource toResourceFromEntity(SubscriptionPlan entity) {
        return new SubscriptionPlanResource(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getDescription(),
                entity.getPriceAmount(),
                entity.getCurrency(),
                entity.getBillingPeriod(),
                entity.getMaxDoctors(),
                entity.getMaxSupervisors(),
                entity.getMaxTeams(),
                entity.getMaxWorkAreas(),
                entity.getMonthlyInvitations(),
                entity.getDataHistoryDays(),
                entity.getSupportLevel(),
                entity.getStatus()
        );
    }
}