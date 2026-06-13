package com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.assemblers;

import com.vitalwatch.center.platform.subscriptions.domain.model.aggregates.SubscriptionPlan;
import com.vitalwatch.center.platform.subscriptions.domain.model.valueobjects.Money;
import com.vitalwatch.center.platform.subscriptions.domain.model.valueobjects.PlanLimits;
import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.entities.SubscriptionPlanPersistenceEntity;

/**
 * Assembler between SubscriptionPlan domain and persistence representations.
 */
public final class SubscriptionPlanPersistenceAssembler {

    private SubscriptionPlanPersistenceAssembler() {
    }

    public static SubscriptionPlan toDomainFromPersistence(SubscriptionPlanPersistenceEntity entity) {
        return new SubscriptionPlan(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getDescription(),
                new Money(entity.getPriceAmount(), entity.getCurrency()),
                entity.getBillingPeriod(),
                new PlanLimits(
                        entity.getMaxDoctors(),
                        entity.getMaxSupervisors(),
                        entity.getMaxTeams(),
                        entity.getMaxWorkAreas(),
                        entity.getMonthlyInvitations(),
                        entity.getDataHistoryDays()
                ),
                entity.getSupportLevel(),
                entity.getStatus()
        );
    }

    public static SubscriptionPlanPersistenceEntity toPersistenceFromDomain(SubscriptionPlan aggregate) {
        var entity = new SubscriptionPlanPersistenceEntity();
        entity.setId(aggregate.getId());
        entity.setCode(aggregate.getCode());
        entity.setName(aggregate.getName());
        entity.setDescription(aggregate.getDescription());
        entity.setPriceAmount(aggregate.getPriceAmount());
        entity.setCurrency(aggregate.getCurrency());
        entity.setBillingPeriod(aggregate.getBillingPeriod());
        entity.setMaxDoctors(aggregate.getMaxDoctors());
        entity.setMaxSupervisors(aggregate.getMaxSupervisors());
        entity.setMaxTeams(aggregate.getMaxTeams());
        entity.setMaxWorkAreas(aggregate.getMaxWorkAreas());
        entity.setMonthlyInvitations(aggregate.getMonthlyInvitations());
        entity.setDataHistoryDays(aggregate.getDataHistoryDays());
        entity.setSupportLevel(aggregate.getSupportLevel());
        entity.setStatus(aggregate.getStatus());
        return entity;
    }
}