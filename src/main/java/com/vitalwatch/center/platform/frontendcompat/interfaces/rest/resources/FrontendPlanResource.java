package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

/**
 * Frontend-compatible response for subscription plans.
 */
@Schema(name = "FrontendPlanResponse", description = "Plan response compatible with the Angular frontend")
public record FrontendPlanResource(
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
        List<String> featureKeys,
        List<String> enabledModules,
        List<String> disabledModules
) {
}