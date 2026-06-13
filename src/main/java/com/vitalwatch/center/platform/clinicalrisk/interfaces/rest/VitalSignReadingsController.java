package com.vitalwatch.center.platform.clinicalrisk.interfaces.rest;

import com.vitalwatch.center.platform.clinicalrisk.application.commandservices.ClinicalRiskCommandService;
import com.vitalwatch.center.platform.clinicalrisk.application.queryservices.ClinicalRiskQueryService;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.queries.GetVitalSignReadingsByUserAccountIdQuery;
import com.vitalwatch.center.platform.clinicalrisk.interfaces.rest.resources.RegisterVitalSignReadingResource;
import com.vitalwatch.center.platform.clinicalrisk.interfaces.rest.resources.VitalSignReadingResource;
import com.vitalwatch.center.platform.clinicalrisk.interfaces.rest.transform.RegisterVitalSignReadingCommandFromResourceAssembler;
import com.vitalwatch.center.platform.clinicalrisk.interfaces.rest.transform.VitalSignReadingResourceFromEntityAssembler;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
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
 * REST controller for vital sign readings.
 */
@RestController
@RequestMapping(value = "/api/v1/vital-sign-readings", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Vital Sign Readings", description = "Vital sign and workload data endpoints")
public class VitalSignReadingsController {

    private final ClinicalRiskCommandService clinicalRiskCommandService;
    private final ClinicalRiskQueryService clinicalRiskQueryService;

    public VitalSignReadingsController(
            ClinicalRiskCommandService clinicalRiskCommandService,
            ClinicalRiskQueryService clinicalRiskQueryService
    ) {
        this.clinicalRiskCommandService = clinicalRiskCommandService;
        this.clinicalRiskQueryService = clinicalRiskQueryService;
    }

    @PostMapping
    @Operation(summary = "Register vital sign reading", description = "Registers physiological and workload data for fatigue monitoring.")
    public ResponseEntity<?> registerVitalSignReading(@Valid @RequestBody RegisterVitalSignReadingResource resource) {
        var command = RegisterVitalSignReadingCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = clinicalRiskCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                VitalSignReadingResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED
        );
    }

    @GetMapping(params = "userAccountId")
    @Operation(summary = "Get readings by user account", description = "Retrieves vital sign readings for a user account.")
    public ResponseEntity<List<VitalSignReadingResource>> getVitalSignReadingsByUserAccountId(
            @RequestParam @Positive Long userAccountId
    ) {
        var readings = clinicalRiskQueryService.handle(new GetVitalSignReadingsByUserAccountIdQuery(userAccountId));

        var resources = readings.stream()
                .map(VitalSignReadingResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }
}