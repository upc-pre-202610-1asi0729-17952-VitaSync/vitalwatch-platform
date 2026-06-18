package com.vitalwatch.center.platform.frontendcompat.interfaces.rest;

import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.CreateFrontendOrganizationResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendOrganizationResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.transform.FrontendOrganizationResourceFromEntityAssembler;
import com.vitalwatch.center.platform.iam.domain.model.aggregates.HospitalWorkspace;
import com.vitalwatch.center.platform.iam.domain.model.enums.HospitalWorkspaceStatus;
import com.vitalwatch.center.platform.iam.domain.model.valueobjects.EmailAddress;
import com.vitalwatch.center.platform.iam.domain.model.valueobjects.HospitalWorkspaceName;
import com.vitalwatch.center.platform.iam.domain.model.valueobjects.Ruc;
import com.vitalwatch.center.platform.iam.domain.repositories.HospitalWorkspaceRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Frontend compatibility controller for organization endpoints expected by Angular.
 */
@RestController
@RequestMapping(value = "/api/v1/organizations", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Frontend Compatibility - Organizations", description = "Organization endpoints compatible with the Angular frontend")
public class OrganizationsCompatibilityController {

    private final HospitalWorkspaceRepository hospitalWorkspaceRepository;

    public OrganizationsCompatibilityController(HospitalWorkspaceRepository hospitalWorkspaceRepository) {
        this.hospitalWorkspaceRepository = hospitalWorkspaceRepository;
    }

    @GetMapping
    @Operation(summary = "Get frontend-compatible organizations")
    public ResponseEntity<List<FrontendOrganizationResource>> getOrganizations() {
        var organizations = hospitalWorkspaceRepository.findAll()
                .stream()
                .map(FrontendOrganizationResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(organizations);
    }

    @GetMapping("/{organizationId}")
    @Operation(summary = "Get frontend-compatible organization by id")
    public ResponseEntity<FrontendOrganizationResource> getOrganizationById(
            @PathVariable @Positive Long organizationId
    ) {
        return hospitalWorkspaceRepository.findById(organizationId)
                .map(FrontendOrganizationResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create frontend-compatible organization")
    public ResponseEntity<FrontendOrganizationResource> createOrganization(
            @Valid @RequestBody CreateFrontendOrganizationResource resource
    ) {
        var existingWorkspace = hospitalWorkspaceRepository.findByRuc(new Ruc(resource.ruc()));

        if (existingWorkspace.isPresent()) {
            var response = FrontendOrganizationResourceFromEntityAssembler.toResourceFromEntity(
                    existingWorkspace.get(),
                    resource.address(),
                    resource.phone(),
                    resource.planId()
            );

            return ResponseEntity.ok(response);
        }

        var workspace = new HospitalWorkspace(
                null,
                new HospitalWorkspaceName(resource.name()),
                new Ruc(resource.ruc()),
                1L,
                new EmailAddress("pending-admin-" + resource.ruc() + "@vitalwatch.local"),
                HospitalWorkspaceStatus.ACTIVE
        );

        var savedWorkspace = hospitalWorkspaceRepository.save(workspace);

        var response = FrontendOrganizationResourceFromEntityAssembler.toResourceFromEntity(
                savedWorkspace,
                resource.address(),
                resource.phone(),
                resource.planId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}