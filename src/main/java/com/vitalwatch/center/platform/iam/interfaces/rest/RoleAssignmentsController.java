package com.vitalwatch.center.platform.iam.interfaces.rest;

import com.vitalwatch.center.platform.iam.application.commandservices.IamCommandService;
import com.vitalwatch.center.platform.iam.interfaces.rest.resources.AssignUserRoleResource;
import com.vitalwatch.center.platform.iam.interfaces.rest.transform.AssignUserRoleCommandFromResourceAssembler;
import com.vitalwatch.center.platform.iam.interfaces.rest.transform.UserAccountResourceFromEntityAssembler;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for role assignments.
 */
@RestController
@RequestMapping(value = "/api/v1/role-assignments", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Role Assignments", description = "Institutional role assignment endpoints")
public class RoleAssignmentsController {

    private final IamCommandService iamCommandService;

    public RoleAssignmentsController(IamCommandService iamCommandService) {
        this.iamCommandService = iamCommandService;
    }

    @PatchMapping("/{userAccountId}")
    @Operation(summary = "Assign user role", description = "Updates the institutional role of a user account.")
    public ResponseEntity<?> assignUserRole(
            @PathVariable @Positive Long userAccountId,
            @Valid @RequestBody AssignUserRoleResource resource
    ) {
        var command = AssignUserRoleCommandFromResourceAssembler.toCommandFromResource(userAccountId, resource);
        var result = iamCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                UserAccountResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK
        );
    }
}