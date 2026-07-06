package com.vitalwatch.center.platform.subscriptions.interfaces.rest.transform;

import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.entities.PlanJpaEntity;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources.PlanResource;

/**
 * Assembler used to convert plan entities into REST resources.
 */
public final class PlanResourceFromEntityAssembler {

    private PlanResourceFromEntityAssembler() {
    }

    public static PlanResource toResourceFromEntity(PlanJpaEntity entity) {
        return new PlanResource(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getCurrency(),
                entity.getBillingPeriod(),
                entity.getMaxDoctors(),
                entity.getMaxSupervisors(),
                entity.getMaxTeams(),
                entity.getMaxWorkAreas(),
                entity.getMonthlyInvitations(),
                entity.getDataHistoryDays(),
                entity.getSupportLevel(),
                entity.getRecommended(),
                entity.getEnabled(),
                entity.getFeatureKeys(),
                entity.getDisabledModuleKeys()
        );
    }
}