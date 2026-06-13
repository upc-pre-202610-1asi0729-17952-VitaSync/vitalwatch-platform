package com.vitalwatch.center.platform.subscriptions.domain.model.commands;

import com.vitalwatch.center.platform.subscriptions.domain.model.enums.BillingPeriod;
import com.vitalwatch.center.platform.subscriptions.domain.model.enums.SubscriptionPlanCode;
import com.vitalwatch.center.platform.subscriptions.domain.model.enums.SupportLevel;

import java.math.BigDecimal;

/**
 * Command used to create a subscription plan.
 */
public record CreateSubscriptionPlanCommand(
        SubscriptionPlanCode code,
        String name,
        String description,
        BigDecimal priceAmount,
        String currency,
        BillingPeriod billingPeriod,
        Integer maxDoctors,
        Integer maxSupervisors,
        Integer maxTeams,
        Integer maxWorkAreas,
        Integer monthlyInvitations,
        Integer dataHistoryDays,
        SupportLevel supportLevel
) {
}