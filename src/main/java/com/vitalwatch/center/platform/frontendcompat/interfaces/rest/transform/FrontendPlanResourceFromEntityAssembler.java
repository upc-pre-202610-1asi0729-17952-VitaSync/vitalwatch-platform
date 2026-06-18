package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.transform;

import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendPlanResource;
import com.vitalwatch.center.platform.subscriptions.domain.model.aggregates.SubscriptionPlan;

import java.util.List;

/**
 * Assembler to expose SubscriptionPlan using the contract expected by the Angular frontend.
 */
public final class FrontendPlanResourceFromEntityAssembler {

    private FrontendPlanResourceFromEntityAssembler() {
    }

    public static FrontendPlanResource toResourceFromEntity(SubscriptionPlan entity) {
        var code = entity.getCode().name().toLowerCase();
        var billingPeriod = entity.getBillingPeriod().name().toLowerCase();
        var supportLevel = entity.getSupportLevel().name();

        return new FrontendPlanResource(
                entity.getId(),
                code,
                entity.getName(),
                entity.getDescription(),
                entity.getPriceAmount(),
                entity.getCurrency(),
                billingPeriod,
                entity.getMaxDoctors(),
                entity.getMaxSupervisors(),
                entity.getMaxTeams(),
                entity.getMaxWorkAreas(),
                entity.getMonthlyInvitations(),
                entity.getDataHistoryDays(),
                supportLevel,
                isRecommended(code),
                featureKeysFor(code),
                enabledModulesFor(code),
                disabledModulesFor(code)
        );
    }

    private static Boolean isRecommended(String code) {
        return "professional".equals(code);
    }

    private static List<String> featureKeysFor(String code) {
        return switch (code) {
            case "basic" -> List.of(
                    "features.basic.staff-monitoring",
                    "features.basic.shift-management",
                    "features.basic.basic-alerts"
            );
            case "professional" -> List.of(
                    "features.professional.advanced-risk",
                    "features.professional.recovery-plans",
                    "features.professional.audit-reports"
            );
            case "enterprise" -> List.of(
                    "features.enterprise.unlimited-users",
                    "features.enterprise.compliance",
                    "features.enterprise.priority-support"
            );
            default -> List.of();
        };
    }

    private static List<String> enabledModulesFor(String code) {
        return switch (code) {
            case "basic" -> List.of(
                    "iam",
                    "subscriptions",
                    "shifts",
                    "clinical-risk"
            );
            case "professional" -> List.of(
                    "iam",
                    "subscriptions",
                    "shifts",
                    "clinical-risk",
                    "incidents",
                    "staff-recovery",
                    "audit"
            );
            case "enterprise" -> List.of(
                    "iam",
                    "subscriptions",
                    "shifts",
                    "clinical-risk",
                    "incidents",
                    "staff-recovery",
                    "audit",
                    "compliance"
            );
            default -> List.of();
        };
    }

    private static List<String> disabledModulesFor(String code) {
        return switch (code) {
            case "basic" -> List.of(
                    "staff-recovery",
                    "audit",
                    "compliance"
            );
            case "professional" -> List.of(
                    "compliance"
            );
            case "enterprise" -> List.of();
            default -> List.of();
        };
    }
}