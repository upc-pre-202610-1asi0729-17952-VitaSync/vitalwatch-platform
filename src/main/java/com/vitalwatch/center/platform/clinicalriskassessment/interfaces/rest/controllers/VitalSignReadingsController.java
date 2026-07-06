package com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.controllers;

import com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.entities.VitalSignReadingJpaEntity;
import com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.repositories.VitalSignReadingJpaRepository;
import com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.resources.VitalSignReadingResource;
import com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.transform.VitalSignReadingResourceFromEntityAssembler;
import com.vitalwatch.center.platform.shared.application.i18n.MessageResolver;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

/**
 * REST controller for vital sign readings.
 */
@RestController
@RequestMapping("/vitalSignReadings")
@Tag(name = "Vital Sign Readings", description = "Vital sign reading endpoints")
public class VitalSignReadingsController {

    private final VitalSignReadingJpaRepository vitalSignReadingRepository;
    private final MessageResolver messageResolver;

    public VitalSignReadingsController(
            VitalSignReadingJpaRepository vitalSignReadingRepository,
            MessageResolver messageResolver
    ) {
        this.vitalSignReadingRepository = vitalSignReadingRepository;
        this.messageResolver = messageResolver;
    }

    @GetMapping
    @Operation(summary = "Get all vital sign readings or filter by organization and user")
    public ResponseEntity<List<VitalSignReadingResource>> getVitalSignReadings(
            @RequestParam(required = false) Long organizationId,
            @RequestParam(required = false) Long userId
    ) {
        var readings = organizationId != null && userId != null
                ? vitalSignReadingRepository.findByOrganizationIdAndUserIdOrderByRecordedAtDesc(
                organizationId,
                userId
        )
                : organizationId != null
                  ? vitalSignReadingRepository.findByOrganizationIdOrderByRecordedAtDesc(organizationId)
                  : vitalSignReadingRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(VitalSignReadingJpaEntity::getRecordedAt).reversed())
                .toList();

        var resources = readings.stream()
                .map(VitalSignReadingResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{vitalSignReadingId}")
    @Operation(summary = "Get vital sign reading by id")
    public ResponseEntity<?> getVitalSignReadingById(@PathVariable Long vitalSignReadingId) {
        var reading = vitalSignReadingRepository.findById(vitalSignReadingId);

        if (reading.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("clinical.vitalSignReading.notFound")
                    )
            );
        }

        return ResponseEntity.ok(
                VitalSignReadingResourceFromEntityAssembler.toResourceFromEntity(reading.get())
        );
    }
}