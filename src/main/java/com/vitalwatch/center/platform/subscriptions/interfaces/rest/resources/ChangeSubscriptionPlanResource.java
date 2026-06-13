package com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Resource used to change the plan of a hospital subscription.
 */
@Schema(name = "ChangeSubscriptionPlanRequest", description = "Request payload for changing a hospital subscription plan")
public record ChangeSubscriptionPlanResource(

        @NotNull
        @Positive
        @Schema(description = "New subscription plan id", example = "2")
        Long newSubscriptionPlanId
) {
}