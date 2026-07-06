package com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.scheduling;

import com.vitalwatch.center.platform.clinicalriskassessment.application.internal.services.ClinicalSimulationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Optional scheduler that simulates wearable readings periodically.
 */
@Component
@ConditionalOnProperty(
        name = "application.simulation.iot.enabled",
        havingValue = "true"
)
public class ClinicalSimulationScheduler {

    private final ClinicalSimulationService clinicalSimulationService;

    public ClinicalSimulationScheduler(
            ClinicalSimulationService clinicalSimulationService
    ) {
        this.clinicalSimulationService = clinicalSimulationService;
    }

    @Scheduled(fixedDelayString = "${application.simulation.iot.fixed-rate-ms:10000}")
    public void simulateWearableReadings() {
        clinicalSimulationService.simulateAllOrganizations();
    }
}