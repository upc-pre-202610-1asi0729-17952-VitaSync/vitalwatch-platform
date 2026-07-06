package com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources;

import java.math.BigDecimal;
import java.util.List;

/**
 * Resource used to expose subscription plan data through the REST API.
 */
public record PlanResource(
        Long id,
        String code,
        String name,
        String description,
        BigDecimal price,
        String currency,
        String billingPeriod,
        Integer maxDoctors,
        Integer maxSupervisors,
        Integer maxTeams,
        Integer maxWorkAreas,
        Integer monthlyInvitations,
        Integer dataHistoryDays,
        String supportLevel,
        Boolean recommended,
        Boolean enabled,
        List<String> featureKeys,
        List<String> disabledModuleKeys
) {
}