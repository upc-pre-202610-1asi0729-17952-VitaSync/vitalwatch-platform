package com.vitalwatch.center.platform.frontendcompat.interfaces.rest;

import com.vitalwatch.center.platform.clinicalrisk.domain.model.aggregates.ClinicalRiskAssessment;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.aggregates.VitalSignReading;
import com.vitalwatch.center.platform.clinicalrisk.domain.repositories.ClinicalRiskAssessmentRepository;
import com.vitalwatch.center.platform.clinicalrisk.domain.repositories.VitalSignReadingRepository;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendClinicalAlertResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendVitalSignAnomalyResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Frontend compatibility controller for clinical alerts and vital sign anomalies.
 */
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Frontend API - Clinical Alerts", description = "Clinical alert and anomaly endpoints compatible with Angular")
public class ClinicalAlertsCompatibilityController {

    private final ClinicalRiskAssessmentRepository clinicalRiskAssessmentRepository;
    private final VitalSignReadingRepository vitalSignReadingRepository;

    public ClinicalAlertsCompatibilityController(
            ClinicalRiskAssessmentRepository clinicalRiskAssessmentRepository,
            VitalSignReadingRepository vitalSignReadingRepository
    ) {
        this.clinicalRiskAssessmentRepository = clinicalRiskAssessmentRepository;
        this.vitalSignReadingRepository = vitalSignReadingRepository;
    }

    @GetMapping("/clinicalAlerts")
    @Operation(summary = "Get frontend-compatible clinical alerts")
    public ResponseEntity<List<FrontendClinicalAlertResource>> getClinicalAlerts(
            @RequestParam(required = false) @Positive Long organizationId,
            @RequestParam(required = false) @Positive Long hospitalWorkspaceId,
            @RequestParam(required = false) @Positive Long userAccountId,
            @RequestParam(required = false) @Positive Long userId
    ) {
        var selectedWorkspaceId = organizationId != null ? organizationId : hospitalWorkspaceId;
        var selectedUserId = userAccountId != null ? userAccountId : userId;

        List<ClinicalRiskAssessment> assessments;

        if (selectedUserId != null) {
            assessments = clinicalRiskAssessmentRepository.findLatestByUserAccountId(selectedUserId)
                    .map(List::of)
                    .orElseGet(List::of);
        } else if (selectedWorkspaceId != null) {
            assessments = clinicalRiskAssessmentRepository.findAllByHospitalWorkspaceId(selectedWorkspaceId);
        } else {
            assessments = List.of();
        }

        var alerts = assessments.stream()
                .filter(this::isAlertAssessment)
                .map(this::toClinicalAlert)
                .toList();

        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/vitalSignAnomalies")
    @Operation(summary = "Get frontend-compatible vital sign anomalies")
    public ResponseEntity<List<FrontendVitalSignAnomalyResource>> getVitalSignAnomalies(
            @RequestParam(required = false) @Positive Long organizationId,
            @RequestParam(required = false) @Positive Long hospitalWorkspaceId,
            @RequestParam(required = false) @Positive Long userAccountId,
            @RequestParam(required = false) @Positive Long userId
    ) {
        var selectedWorkspaceId = organizationId != null ? organizationId : hospitalWorkspaceId;
        var selectedUserId = userAccountId != null ? userAccountId : userId;

        List<VitalSignReading> readings;

        if (selectedUserId != null) {
            readings = vitalSignReadingRepository.findAllByUserAccountId(selectedUserId);
        } else if (selectedWorkspaceId != null) {
            readings = vitalSignReadingRepository.findAllByHospitalWorkspaceId(selectedWorkspaceId);
        } else {
            readings = List.of();
        }

        var anomalies = readings.stream()
                .flatMap(reading -> toVitalSignAnomalies(reading).stream())
                .toList();

        return ResponseEntity.ok(anomalies);
    }

    private boolean isAlertAssessment(ClinicalRiskAssessment assessment) {
        var riskLevel = assessment.getRiskLevel().name();
        return "HIGH".equals(riskLevel) || "CRITICAL".equals(riskLevel);
    }

    private FrontendClinicalAlertResource toClinicalAlert(ClinicalRiskAssessment assessment) {
        var severity = assessment.getRiskLevel().name();
        var message = "Clinical risk detected with fatigue score " + assessment.getFatigueScore();

        return new FrontendClinicalAlertResource(
                assessment.getId(),
                assessment.getHospitalWorkspaceId(),
                assessment.getHospitalWorkspaceId(),
                assessment.getUserAccountId(),
                assessment.getUserAccountId(),
                assessment.getId(),
                "CLINICAL_RISK",
                severity,
                message,
                assessment.getStatus().name(),
                assessment.getAssessedAt()
        );
    }

    private List<FrontendVitalSignAnomalyResource> toVitalSignAnomalies(VitalSignReading reading) {
        var anomalies = new ArrayList<FrontendVitalSignAnomalyResource>();
        long baseId = reading.getId() != null ? reading.getId() * 10 : 0L;

        if (reading.getHeartRateBpm() > 100) {
            anomalies.add(toAnomaly(
                    baseId + 1,
                    reading,
                    "HIGH_HEART_RATE",
                    reading.getHeartRateBpm().doubleValue(),
                    100.0,
                    reading.getHeartRateBpm() > 120 ? "CRITICAL" : "HIGH",
                    "Heart rate is above the recommended threshold"
            ));
        }

        if (reading.getHeartRateBpm() < 50) {
            anomalies.add(toAnomaly(
                    baseId + 2,
                    reading,
                    "LOW_HEART_RATE",
                    reading.getHeartRateBpm().doubleValue(),
                    50.0,
                    "MODERATE",
                    "Heart rate is below the recommended threshold"
            ));
        }

        if (reading.getSleepHoursLast24h() < 6) {
            anomalies.add(toAnomaly(
                    baseId + 3,
                    reading,
                    "LOW_SLEEP",
                    reading.getSleepHoursLast24h(),
                    6.0,
                    reading.getSleepHoursLast24h() < 4 ? "CRITICAL" : "HIGH",
                    "Sleep hours during the last 24 hours are below the recommended threshold"
            ));
        }

        if (reading.getShiftHoursLast24h() > 12) {
            anomalies.add(toAnomaly(
                    baseId + 4,
                    reading,
                    "EXTENDED_SHIFT",
                    reading.getShiftHoursLast24h(),
                    12.0,
                    reading.getShiftHoursLast24h() > 16 ? "CRITICAL" : "HIGH",
                    "Shift hours during the last 24 hours are above the recommended threshold"
            ));
        }

        if (reading.getSelfReportedFatigueLevel() >= 4) {
            anomalies.add(toAnomaly(
                    baseId + 5,
                    reading,
                    "SELF_REPORTED_FATIGUE",
                    reading.getSelfReportedFatigueLevel().doubleValue(),
                    4.0,
                    reading.getSelfReportedFatigueLevel() == 5 ? "CRITICAL" : "HIGH",
                    "Self-reported fatigue level is high"
            ));
        }

        return anomalies;
    }

    private FrontendVitalSignAnomalyResource toAnomaly(
            Long id,
            VitalSignReading reading,
            String type,
            Double value,
            Double threshold,
            String severity,
            String message
    ) {
        return new FrontendVitalSignAnomalyResource(
                id,
                reading.getHospitalWorkspaceId(),
                reading.getHospitalWorkspaceId(),
                reading.getUserAccountId(),
                reading.getUserAccountId(),
                reading.getId(),
                type,
                severity,
                type,
                value,
                threshold,
                message,
                reading.getRecordedAt()
        );
    }
}