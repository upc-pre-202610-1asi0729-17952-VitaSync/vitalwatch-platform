package com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources;

import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.PreventiveActionStatus;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

/**
 * Resource used to update preventive action status.
 */
public record UpdatePreventiveActionStatusResource(
        @NotNull
        PreventiveActionStatus status,

        OffsetDateTime completedAt
) {
}