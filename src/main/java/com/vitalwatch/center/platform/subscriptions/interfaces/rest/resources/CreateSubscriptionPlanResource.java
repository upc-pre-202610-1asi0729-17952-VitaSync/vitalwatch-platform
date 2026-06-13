package com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources;

import com.vitalwatch.center.platform.subscriptions.domain.model.enums.BillingPeriod;
import com.vitalwatch.center.platform.subscriptions.domain.model.enums.SubscriptionPlanCode;
import com.vitalwatch.center.platform.subscriptions.domain.model.enums.SupportLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * Resource used to create a VitalWatch subscription plan.
 */
@Schema(name = "CreateSubscriptionPlanRequest", description = "Request payload for creating a subscription plan")
public record CreateSubscriptionPlanResource(

        @NotNull
        @Schema(description = "Plan code", example = "BASIC")
        SubscriptionPlanCode code,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Plan name", example = "Basic")
        String name,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Plan description", example = "Essential plan for small clinics")
        String description,

        @NotNull
        @DecimalMin(value = "0.00")
        @Schema(description = "Plan price amount", example = "99.90")
        BigDecimal priceAmount,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Currency code", example = "PEN")
        String currency,

        @NotNull
        @Schema(description = "Billing period", example = "MONTHLY")
        BillingPeriod billingPeriod,

        @NotNull
        @Min(0)
        @Schema(description = "Maximum doctors allowed", example = "10")
        Integer maxDoctors,

        @NotNull
        @Min(0)
        @Schema(description = "Maximum supervisors allowed", example = "2")
        Integer maxSupervisors,

        @NotNull
        @Min(0)
        @Schema(description = "Maximum teams allowed", example = "2")
        Integer maxTeams,

        @NotNull
        @Min(0)
        @Schema(description = "Maximum work areas allowed", example = "3")
        Integer maxWorkAreas,

        @NotNull
        @Min(0)
        @Schema(description = "Monthly invitations allowed", example = "20")
        Integer monthlyInvitations,

        @NotNull
        @Min(0)
        @Schema(description = "Data history days allowed", example = "30")
        Integer dataHistoryDays,

        @NotNull
        @Schema(description = "Support level", example = "BASIC")
        SupportLevel supportLevel
) {
}