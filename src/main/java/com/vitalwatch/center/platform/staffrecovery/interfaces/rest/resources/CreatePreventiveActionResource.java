package com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources;

import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.PreventiveActionStatus;
import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.PreventiveActionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

/**
 * Resource used to create a preventive action.
 */
public record CreatePreventiveActionResource(
        @NotNull
        Long organizationId,

        @NotNull
        Long supervisorId,

        @NotNull
        Long userId,

        @NotNull
        PreventiveActionType type,

        PreventiveActionStatus status,

        @NotBlank
        @Size(max = 1000)
        String notes,

        OffsetDateTime createdAt,

        OffsetDateTime completedAt
) {
}