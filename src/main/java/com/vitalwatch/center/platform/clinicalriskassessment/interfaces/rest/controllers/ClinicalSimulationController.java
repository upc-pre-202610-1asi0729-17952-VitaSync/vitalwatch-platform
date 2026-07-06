package com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.controllers;

import com.vitalwatch.center.platform.clinicalriskassessment.application.internal.services.ClinicalSimulationService;
import com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.resources.SimulationTickResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller used to manually trigger simulated IoT readings.
 */
@RestController
@RequestMapping("/clinicalSimulation")
@Tag(name = "Clinical Simulation", description = "IoT wearable simulation endpoints")
public class ClinicalSimulationController {

    private final ClinicalSimulationService clinicalSimulationService;

    public ClinicalSimulationController(
            ClinicalSimulationService clinicalSimulationService
    ) {
        this.clinicalSimulationService = clinicalSimulationService;
    }

    @PostMapping("/tick")
    @Operation(summary = "Generate simulated IoT readings for one organization")
    public ResponseEntity<SimulationTickResource> simulateOrganization(
            @RequestParam Long organizationId
    ) {
        return ResponseEntity.ok(
                clinicalSimulationService.simulateOrganization(organizationId)
        );
    }

    @PostMapping("/tick-all")
    @Operation(summary = "Generate simulated IoT readings for all organizations")
    public ResponseEntity<SimulationTickResource> simulateAllOrganizations() {
        return ResponseEntity.ok(
                clinicalSimulationService.simulateAllOrganizations()
        );
    }
}