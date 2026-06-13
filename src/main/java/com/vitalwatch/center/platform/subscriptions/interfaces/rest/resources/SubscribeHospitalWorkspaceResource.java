package com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Resource used to subscribe a hospital workspace to a plan.
 */
@Schema(name = "SubscribeHospitalWorkspaceRequest", description = "Request payload for subscribing a hospital workspace")
public record SubscribeHospitalWorkspaceResource(

        @NotNull
        @Positive
        @Schema(description = "Hospital workspace id", example = "1")
        Long hospitalWorkspaceId,

        @NotNull
        @Positive
        @Schema(description = "Subscription plan id", example = "1")
        Long subscriptionPlanId
) {
}