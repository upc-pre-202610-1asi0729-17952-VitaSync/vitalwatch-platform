package com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Resource used to complete a recovery action.
 */
@Schema(name = "CompleteRecoveryActionRequest", description = "Request payload for completing a recovery action")
public record CompleteRecoveryActionResource(

        @NotNull
        @Positive
        @Schema(description = "User account id that completes the action", example = "1")
        Long completedByUserAccountId
) {
}