package com.vitalwatch.center.platform.iam.catalogs.interfaces.rest.controllers;

import com.vitalwatch.center.platform.iam.catalogs.infrastructure.persistence.jpa.repositories.SpecialtyJpaRepository;
import com.vitalwatch.center.platform.iam.catalogs.interfaces.rest.resources.SpecialtyResource;
import com.vitalwatch.center.platform.iam.catalogs.interfaces.rest.transform.SpecialtyResourceFromEntityAssembler;
import com.vitalwatch.center.platform.shared.application.i18n.MessageResolver;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for medical specialty catalog.
 */
@RestController
@RequestMapping("/specialties")
@Tag(name = "Specialties", description = "Medical specialty catalog endpoints")
public class SpecialtiesController {

    private final SpecialtyJpaRepository specialtyRepository;
    private final MessageResolver messageResolver;

    public SpecialtiesController(
            SpecialtyJpaRepository specialtyRepository,
            MessageResolver messageResolver
    ) {
        this.specialtyRepository = specialtyRepository;
        this.messageResolver = messageResolver;
    }

    @GetMapping
    @Operation(summary = "Get all specialties")
    public ResponseEntity<List<SpecialtyResource>> getSpecialties() {
        var specialties = specialtyRepository.findAllByOrderByNameAsc();

        var resources = specialties.stream()
                .map(SpecialtyResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{specialtyId}")
    @Operation(summary = "Get specialty by id")
    public ResponseEntity<?> getSpecialtyById(@PathVariable Long specialtyId) {
        var specialty = specialtyRepository.findById(specialtyId);

        if (specialty.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("iam.specialty.notFound")
                    )
            );
        }

        var resource = SpecialtyResourceFromEntityAssembler.toResourceFromEntity(
                specialty.get()
        );

        return ResponseEntity.ok(resource);
    }
}