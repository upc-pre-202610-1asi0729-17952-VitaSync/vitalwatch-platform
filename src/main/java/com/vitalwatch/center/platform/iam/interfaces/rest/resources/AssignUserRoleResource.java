package com.vitalwatch.center.platform.iam.interfaces.rest.resources;

import com.vitalwatch.center.platform.iam.domain.model.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Resource used to assign or update a user role.
 */
@Schema(name = "AssignUserRoleRequest", description = "Request payload for updating a user institutional role")
public record AssignUserRoleResource(

        @NotNull
        @Schema(description = "New institutional role", example = "CLINICAL_SUPERVISOR")
        UserRole role
) {
}