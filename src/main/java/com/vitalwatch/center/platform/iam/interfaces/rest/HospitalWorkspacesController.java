package com.vitalwatch.center.platform.iam.interfaces.rest;

import com.vitalwatch.center.platform.iam.application.commandservices.IamCommandService;
import com.vitalwatch.center.platform.iam.application.queryservices.IamQueryService;
import com.vitalwatch.center.platform.iam.domain.model.queries.GetHospitalWorkspaceByIdQuery;
import com.vitalwatch.center.platform.iam.interfaces.rest.resources.CreateHospitalWorkspaceResource;
import com.vitalwatch.center.platform.iam.interfaces.rest.resources.HospitalWorkspaceResource;
import com.vitalwatch.center.platform.iam.interfaces.rest.transform.CreateHospitalWorkspaceCommandFromResourceAssembler;
import com.vitalwatch.center.platform.iam.interfaces.rest.transform.HospitalWorkspaceResourceFromEntityAssembler;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for hospital workspaces.
 */
@RestController
@RequestMapping(value = "/api/v1/hospital-workspaces", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Hospital Workspaces", description = "Hospital workspace management endpoints")
public class HospitalWorkspacesController {

    private final IamCommandService iamCommandService;
    private final IamQueryService iamQueryService;

    public HospitalWorkspacesController(
            IamCommandService iamCommandService,
            IamQueryService iamQueryService
    ) {
        this.iamCommandService = iamCommandService;
        this.iamQueryService = iamQueryService;
    }

    @PostMapping
    @Operation(summary = "Create hospital workspace", description = "Creates a hospital workspace and its administrator account.")
    public ResponseEntity<?> createHospitalWorkspace(@Valid @RequestBody CreateHospitalWorkspaceResource resource) {
        var command = CreateHospitalWorkspaceCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = iamCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                HospitalWorkspaceResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{hospitalWorkspaceId}")
    @Operation(summary = "Get hospital workspace by id", description = "Retrieves a hospital workspace by id.")
    public ResponseEntity<?> getHospitalWorkspaceById(@PathVariable Long hospitalWorkspaceId) {
        var workspace = iamQueryService.handle(new GetHospitalWorkspaceByIdQuery(hospitalWorkspaceId));

        if (workspace.isEmpty()) {
            var error = ApplicationError.notFound("HospitalWorkspace", hospitalWorkspaceId.toString());
            return ErrorResponseAssembler.toErrorResponseFromApplicationError(error);
        }

        var resource = HospitalWorkspaceResourceFromEntityAssembler.toResourceFromEntity(workspace.get());
        return ResponseEntity.ok(resource);
    }
}