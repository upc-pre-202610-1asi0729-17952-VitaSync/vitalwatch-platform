package com.vitalwatch.center.platform.iam.interfaces.rest.controllers;

import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities.OrganizationJpaEntity;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.OrganizationJpaRepository;
import com.vitalwatch.center.platform.iam.interfaces.rest.resources.CreateOrganizationResource;
import com.vitalwatch.center.platform.iam.interfaces.rest.resources.OrganizationResource;
import com.vitalwatch.center.platform.iam.interfaces.rest.transform.OrganizationResourceFromEntityAssembler;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * REST controller for organization operations.
 */
@RestController
@RequestMapping("/organizations")
@Tag(name = "Organizations", description = "Organization management endpoints")
public class OrganizationsController {

    private final OrganizationJpaRepository organizationRepository;

    public OrganizationsController(OrganizationJpaRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @GetMapping
    @Operation(summary = "Get all organizations")
    public ResponseEntity<List<OrganizationResource>> getAllOrganizations() {
        var organizations = organizationRepository.findAll()
                .stream()
                .map(OrganizationResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(organizations);
    }

    @GetMapping("/{organizationId}")
    @Operation(summary = "Get organization by id")
    public ResponseEntity<?> getOrganizationById(@PathVariable Long organizationId) {
        var organization = organizationRepository.findById(organizationId);

        if (organization.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    ApplicationError.notFound("Organization")
            );
        }

        var resource = OrganizationResourceFromEntityAssembler.toResourceFromEntity(
                organization.get()
        );

        return ResponseEntity.ok(resource);
    }

    @PostMapping
    @Operation(summary = "Create organization")
    public ResponseEntity<?> createOrganization(
            @Valid @RequestBody CreateOrganizationResource resource
    ) {
        if (organizationRepository.existsByRuc(resource.ruc())) {
            return ErrorResponseAssembler.toResponseEntity(
                    ApplicationError.conflict("An organization with this RUC already exists.")
            );
        }

        if (organizationRepository.existsByEmail(resource.email())) {
            return ErrorResponseAssembler.toResponseEntity(
                    ApplicationError.conflict("An organization with this email already exists.")
            );
        }

        var organization = new OrganizationJpaEntity(
                resource.legalName(),
                resource.commercialName(),
                resource.ruc(),
                resource.email(),
                resource.phone(),
                resource.address()
        );

        organization.activate();

        var savedOrganization = organizationRepository.save(organization);

        var organizationResource = OrganizationResourceFromEntityAssembler.toResourceFromEntity(
                savedOrganization
        );

        return ResponseEntity
                .created(URI.create("/organizations/" + savedOrganization.getId()))
                .body(organizationResource);
    }
}