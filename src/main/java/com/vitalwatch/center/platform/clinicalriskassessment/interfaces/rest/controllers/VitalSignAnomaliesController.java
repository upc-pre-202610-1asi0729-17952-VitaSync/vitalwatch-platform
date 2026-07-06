package com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.controllers;

import com.vitalwatch.center.platform.clinicalriskassessment.domain.model.enums.VitalSignAnomalyStatus;
import com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.entities.VitalSignAnomalyJpaEntity;
import com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.repositories.VitalSignAnomalyJpaRepository;
import com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.resources.UpdateVitalSignAnomalyStatusResource;
import com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.resources.VitalSignAnomalyResource;
import com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.transform.VitalSignAnomalyResourceFromEntityAssembler;
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
 * REST controller for vital sign anomalies.
 */
@RestController
@RequestMapping("/vitalSignAnomalies")
@Tag(name = "Vital Sign Anomalies", description = "Vital sign anomaly endpoints")
public class VitalSignAnomaliesController {

    private final VitalSignAnomalyJpaRepository vitalSignAnomalyRepository;
    private final MessageResolver messageResolver;

    public VitalSignAnomaliesController(
            VitalSignAnomalyJpaRepository vitalSignAnomalyRepository,
            MessageResolver messageResolver
    ) {
        this.vitalSignAnomalyRepository = vitalSignAnomalyRepository;
        this.messageResolver = messageResolver;
    }

    @GetMapping
    @Operation(summary = "Get all vital sign anomalies or filter them")
    public ResponseEntity<List<VitalSignAnomalyResource>> getVitalSignAnomalies(
            @RequestParam(required = false) Long organizationId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) VitalSignAnomalyStatus status
    ) {
        var anomalies = organizationId != null && userId != null
                ? vitalSignAnomalyRepository.findByOrganizationIdAndUserIdOrderByDetectedAtDesc(
                organizationId,
                userId
        )
                : organizationId != null && status != null
                  ? vitalSignAnomalyRepository.findByOrganizationIdAndStatusOrderByDetectedAtDesc(
                organizationId,
                status
        )
                  : organizationId != null
                    ? vitalSignAnomalyRepository.findByOrganizationIdOrderByDetectedAtDesc(organizationId)
                    : vitalSignAnomalyRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(VitalSignAnomalyJpaEntity::getDetectedAt).reversed())
                .toList();

        var resources = anomalies.stream()
                .map(VitalSignAnomalyResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{vitalSignAnomalyId}")
    @Operation(summary = "Get vital sign anomaly by id")
    public ResponseEntity<?> getVitalSignAnomalyById(@PathVariable Long vitalSignAnomalyId) {
        var anomaly = vitalSignAnomalyRepository.findById(vitalSignAnomalyId);

        if (anomaly.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("clinical.vitalSignAnomaly.notFound")
                    )
            );
        }

        return ResponseEntity.ok(
                VitalSignAnomalyResourceFromEntityAssembler.toResourceFromEntity(anomaly.get())
        );
    }

    @PatchMapping("/{vitalSignAnomalyId}")
    @Operation(summary = "Update vital sign anomaly status")
    public ResponseEntity<?> updateVitalSignAnomaly(
            @PathVariable Long vitalSignAnomalyId,
            @Valid @RequestBody UpdateVitalSignAnomalyStatusResource resource
    ) {
        var anomaly = vitalSignAnomalyRepository.findById(vitalSignAnomalyId);

        if (anomaly.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("clinical.vitalSignAnomaly.notFound")
                    )
            );
        }

        var foundAnomaly = anomaly.get();

        foundAnomaly.updateStatus(
                resource.status(),
                resource.reviewedAt(),
                resource.reviewedBy()
        );

        var savedAnomaly = vitalSignAnomalyRepository.save(foundAnomaly);

        return ResponseEntity.ok(
                VitalSignAnomalyResourceFromEntityAssembler.toResourceFromEntity(savedAnomaly)
        );
    }
}