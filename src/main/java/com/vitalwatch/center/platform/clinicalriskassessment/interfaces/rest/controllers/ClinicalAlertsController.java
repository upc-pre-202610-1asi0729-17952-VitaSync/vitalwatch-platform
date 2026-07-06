package com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.controllers;

import com.vitalwatch.center.platform.clinicalriskassessment.domain.model.enums.ClinicalAlertStatus;
import com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.entities.ClinicalAlertJpaEntity;
import com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.repositories.ClinicalAlertJpaRepository;
import com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.resources.ClinicalAlertResource;
import com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.resources.UpdateClinicalAlertStatusResource;
import com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.transform.ClinicalAlertResourceFromEntityAssembler;
import com.vitalwatch.center.platform.shared.application.i18n.MessageResolver;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

/**
 * REST controller for clinical alerts.
 */
@RestController
@RequestMapping("/clinicalAlerts")
@Tag(name = "Clinical Alerts", description = "Clinical alert endpoints")
public class ClinicalAlertsController {

    private final ClinicalAlertJpaRepository clinicalAlertRepository;
    private final MessageResolver messageResolver;

    public ClinicalAlertsController(
            ClinicalAlertJpaRepository clinicalAlertRepository,
            MessageResolver messageResolver
    ) {
        this.clinicalAlertRepository = clinicalAlertRepository;
        this.messageResolver = messageResolver;
    }

    @GetMapping
    @Operation(summary = "Get all clinical alerts or filter them")
    public ResponseEntity<List<ClinicalAlertResource>> getClinicalAlerts(
            @RequestParam(required = false) Long organizationId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) ClinicalAlertStatus status
    ) {
        var alerts = organizationId != null && userId != null
                ? clinicalAlertRepository.findByOrganizationIdAndUserIdOrderByCreatedAtDesc(
                organizationId,
                userId
        )
                : organizationId != null && status != null
                  ? clinicalAlertRepository.findByOrganizationIdAndStatusOrderByCreatedAtDesc(
                organizationId,
                status
        )
                  : organizationId != null
                    ? clinicalAlertRepository.findByOrganizationIdOrderByCreatedAtDesc(organizationId)
                    : clinicalAlertRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(ClinicalAlertJpaEntity::getCreatedAt).reversed())
                .toList();

        var resources = alerts.stream()
                .map(ClinicalAlertResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{clinicalAlertId}")
    @Operation(summary = "Get clinical alert by id")
    public ResponseEntity<?> getClinicalAlertById(@PathVariable Long clinicalAlertId) {
        var alert = clinicalAlertRepository.findById(clinicalAlertId);

        if (alert.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("clinical.alert.notFound")
                    )
            );
        }

        return ResponseEntity.ok(
                ClinicalAlertResourceFromEntityAssembler.toResourceFromEntity(alert.get())
        );
    }

    @PatchMapping("/{clinicalAlertId}")
    @Operation(summary = "Update clinical alert status")
    public ResponseEntity<?> updateClinicalAlert(
            @PathVariable Long clinicalAlertId,
            @Valid @RequestBody UpdateClinicalAlertStatusResource resource
    ) {
        var alert = clinicalAlertRepository.findById(clinicalAlertId);

        if (alert.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("clinical.alert.notFound")
                    )
            );
        }

        var foundAlert = alert.get();

        foundAlert.updateStatus(
                resource.status(),
                resource.resolvedAt(),
                resource.resolvedBy()
        );

        var savedAlert = clinicalAlertRepository.save(foundAlert);

        return ResponseEntity.ok(
                ClinicalAlertResourceFromEntityAssembler.toResourceFromEntity(savedAlert)
        );
    }
}