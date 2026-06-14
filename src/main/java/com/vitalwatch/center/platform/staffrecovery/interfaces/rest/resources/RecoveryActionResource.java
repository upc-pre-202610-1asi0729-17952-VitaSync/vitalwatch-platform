package com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources;

import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.RecoveryActionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Resource used to expose recovery action data.
 */
@Schema(name = "RecoveryActionResponse", description = "Staff recovery action information response")
public record RecoveryActionResource(

        @Schema(description = "Recovery action id", example = "1")
        Long id,

        @Schema(description = "Recovery plan id", example = "1")
        Long recoveryPlanId,

        @Schema(description = "Recovery action type", example = "REST_PERIOD")
        RecoveryActionType actionType,

        @Schema(description = "Action notes")
        String notes,

        @Schema(description = "Recommended rest hours", example = "12.0")
        Double recommendedRestHours,

        @Schema(description = "Whether the action is completed", example = "false")
        Boolean completed,

        @Schema(description = "User account id that completed the action")
        Long completedByUserAccountId,

        @Schema(description = "Action creation date")
        Instant createdAt,

        @Schema(description = "Action completion date")
        Instant completedAt
) {
}