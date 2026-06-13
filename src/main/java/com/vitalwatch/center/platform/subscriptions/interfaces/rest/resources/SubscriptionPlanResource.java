package com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources;

import com.vitalwatch.center.platform.subscriptions.domain.model.enums.BillingPeriod;
import com.vitalwatch.center.platform.subscriptions.domain.model.enums.SubscriptionPlanCode;
import com.vitalwatch.center.platform.subscriptions.domain.model.enums.SubscriptionPlanStatus;
import com.vitalwatch.center.platform.subscriptions.domain.model.enums.SupportLevel;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * Resource used to expose subscription plan data.
 */
@Schema(name = "SubscriptionPlanResponse", description = "Subscription plan information response")
public record SubscriptionPlanResource(

        @Schema(description = "Subscription plan id", example = "1")
        Long id,

        @Schema(description = "Plan code", example = "BASIC")
        SubscriptionPlanCode code,

        @Schema(description = "Plan name", example = "Basic")
        String name,

        @Schema(description = "Plan description")
        String description,

        @Schema(description = "Plan price amount", example = "99.90")
        BigDecimal priceAmount,

        @Schema(description = "Currency code", example = "PEN")
        String currency,

        @Schema(description = "Billing period", example = "MONTHLY")
        BillingPeriod billingPeriod,

        @Schema(description = "Maximum doctors allowed", example = "10")
        Integer maxDoctors,

        @Schema(description = "Maximum supervisors allowed", example = "2")
        Integer maxSupervisors,

        @Schema(description = "Maximum teams allowed", example = "2")
        Integer maxTeams,

        @Schema(description = "Maximum work areas allowed", example = "3")
        Integer maxWorkAreas,

        @Schema(description = "Monthly invitations allowed", example = "20")
        Integer monthlyInvitations,

        @Schema(description = "Data history days allowed", example = "30")
        Integer dataHistoryDays,

        @Schema(description = "Support level", example = "BASIC")
        SupportLevel supportLevel,

        @Schema(description = "Plan status", example = "ACTIVE")
        SubscriptionPlanStatus status
) {
}