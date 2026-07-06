package com.vitalwatch.center.platform.iam.catalogs.interfaces.rest.controllers;

import com.vitalwatch.center.platform.iam.catalogs.infrastructure.persistence.jpa.seed.IamCatalogDataSeeder;
import com.vitalwatch.center.platform.iam.catalogs.infrastructure.persistence.jpa.repositories.WorkAreaJpaRepository;
import com.vitalwatch.center.platform.iam.catalogs.interfaces.rest.resources.WorkAreaResource;
import com.vitalwatch.center.platform.iam.catalogs.interfaces.rest.transform.WorkAreaResourceFromEntityAssembler;
import com.vitalwatch.center.platform.shared.application.i18n.MessageResolver;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for medical work area catalog.
 */
@RestController
@RequestMapping("/workAreas")
@Tag(name = "Work Areas", description = "Medical work area catalog endpoints")
public class WorkAreasController {

    private final WorkAreaJpaRepository workAreaRepository;
    private final IamCatalogDataSeeder catalogDataSeeder;
    private final MessageResolver messageResolver;

    public WorkAreasController(
            WorkAreaJpaRepository workAreaRepository,
            IamCatalogDataSeeder catalogDataSeeder,
            MessageResolver messageResolver
    ) {
        this.workAreaRepository = workAreaRepository;
        this.catalogDataSeeder = catalogDataSeeder;
        this.messageResolver = messageResolver;
    }

    @GetMapping
    @Operation(summary = "Get all work areas or work areas by organization id")
    public ResponseEntity<List<WorkAreaResource>> getWorkAreas(
            @RequestParam(required = false) Long organizationId
    ) {
        if (organizationId != null) {
            catalogDataSeeder.ensureWorkAreasForOrganization(organizationId);
        }

        var workAreas = organizationId == null
                ? workAreaRepository.findAllByOrderByNameAsc()
                : workAreaRepository.findByOrganizationIdOrderByNameAsc(organizationId);

        var resources = workAreas.stream()
                .map(WorkAreaResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{workAreaId}")
    @Operation(summary = "Get work area by id")
    public ResponseEntity<?> getWorkAreaById(@PathVariable Long workAreaId) {
        var workArea = workAreaRepository.findById(workAreaId);

        if (workArea.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("iam.workArea.notFound")
                    )
            );
        }

        var resource = WorkAreaResourceFromEntityAssembler.toResourceFromEntity(
                workArea.get()
        );

        return ResponseEntity.ok(resource);
    }
}