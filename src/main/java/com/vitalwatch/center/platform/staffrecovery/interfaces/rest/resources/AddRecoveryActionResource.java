package com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources;

import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.RecoveryActionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Resource used to add a recovery action.
 */
@Schema(name = "AddRecoveryActionRequest", description = "Request payload for adding a recovery action")
public record AddRecoveryActionResource(

        @NotNull
        @Positive
        @Schema(description = "Recovery plan id", example = "1")
        Long recoveryPlanId,

        @NotNull
        @Schema(description = "Recovery action type", example = "REST_PERIOD")
        RecoveryActionType actionType,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Recovery action notes", example = "Assign mandatory rest period and avoid emergency shifts.")
        String notes,

        @NotNull
        @PositiveOrZero
        @Schema(description = "Recommended rest hours", example = "12.0")
        Double recommendedRestHours
) {
}