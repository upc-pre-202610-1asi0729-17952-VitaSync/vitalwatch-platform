package com.vitalwatch.center.platform.frontendcompat.interfaces.rest;

import com.vitalwatch.center.platform.clinicalrisk.domain.model.aggregates.ClinicalRiskAssessment;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.aggregates.VitalSignReading;
import com.vitalwatch.center.platform.clinicalrisk.domain.repositories.ClinicalRiskAssessmentRepository;
import com.vitalwatch.center.platform.clinicalrisk.domain.repositories.VitalSignReadingRepository;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendClinicalAlertResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendVitalSignAnomalyResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.UpdateFrontendClinicalAlertStatusResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.UpdateFrontendVitalSignAnomalyStatusResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
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
                .map(assessment -> toClinicalAlert(assessment, "ACTIVE", null, null))
                .toList();

        return ResponseEntity.ok(alerts);
    }

    @PatchMapping("/clinicalAlerts/{alertId}")
    @Operation(summary = "Update frontend-compatible clinical alert status")
    public ResponseEntity<FrontendClinicalAlertResource> updateClinicalAlertStatus(
            @PathVariable @Positive Long alertId,
            @Valid @RequestBody UpdateFrontendClinicalAlertStatusResource resource
    ) {
        var assessment = clinicalRiskAssessmentRepository.findById(alertId);

        if (assessment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var status = firstNonBlank(resource.status(), "RESOLVED");
        var resolvedAt = resource.resolvedAt() != null ? resource.resolvedAt() : Instant.now();

        return ResponseEntity.ok(
                toClinicalAlert(
                        assessment.get(),
                        status,
                        "RESOLVED".equalsIgnoreCase(status) ? resolvedAt : null,
                        resource.resolvedBy()
                )
        );
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

    @PatchMapping("/vitalSignAnomalies/{anomalyId}")
    @Operation(summary = "Update frontend-compatible vital sign anomaly status")
    public ResponseEntity<FrontendVitalSignAnomalyResource> updateAnomalyStatus(
            @PathVariable @Positive Long anomalyId,
            @Valid @RequestBody UpdateFrontendVitalSignAnomalyStatusResource resource
    ) {
        var readingId = Math.max(1L, anomalyId / 10L);
        var reading = vitalSignReadingRepository.findById(readingId);

        if (reading.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var anomaly = toVitalSignAnomalies(reading.get()).stream()
                .filter(item -> item.id().equals(anomalyId))
                .findFirst()
                .orElseGet(() -> toAnomaly(
                        anomalyId,
                        reading.get(),
                        "HEART_RATE_SPIKE",
                        reading.get().getHeartRateBpm().doubleValue(),
                        100.0,
                        "HIGH",
                        "Vital sign anomaly reviewed from frontend"
                ));

        var status = firstNonBlank(resource.status(), "REVIEWED");
        var reviewedAt = resource.reviewedAt() != null ? resource.reviewedAt() : Instant.now();

        return ResponseEntity.ok(
                new FrontendVitalSignAnomalyResource(
                        anomaly.id(),
                        anomaly.organizationId(),
                        anomaly.hospitalWorkspaceId(),
                        anomaly.userAccountId(),
                        anomaly.userId(),
                        anomaly.vitalSignReadingId(),
                        anomaly.type(),
                        anomaly.severity(),
                        status,
                        anomaly.metric(),
                        anomaly.value(),
                        anomaly.threshold(),
                        anomaly.message(),
                        anomaly.detectedAt(),
                        reviewedAt,
                        resource.reviewedBy()
                )
        );
    }

    private boolean isAlertAssessment(ClinicalRiskAssessment assessment) {
        var riskLevel = assessment.getRiskLevel().name();
        return "HIGH".equals(riskLevel) || "CRITICAL".equals(riskLevel);
    }

    private FrontendClinicalAlertResource toClinicalAlert(
            ClinicalRiskAssessment assessment,
            String status,
            Instant resolvedAt,
            Long resolvedBy
    ) {
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
                "RESOLVED".equalsIgnoreCase(status) ? "RESOLVED" : "ACTIVE",
                assessment.getAssessedAt(),
                assessment.getAssessedAt(),
                resolvedAt,
                resolvedBy
        );
    }

    private List<FrontendVitalSignAnomalyResource> toVitalSignAnomalies(VitalSignReading reading) {
        var anomalies = new ArrayList<FrontendVitalSignAnomalyResource>();
        long baseId = reading.getId() != null ? reading.getId() * 10 : 0L;

        if (reading.getHeartRateBpm() > 100) {
            anomalies.add(toAnomaly(
                    baseId + 1,
                    reading,
                    "HEART_RATE_SPIKE",
                    reading.getHeartRateBpm().doubleValue(),
                    100.0,
                    reading.getHeartRateBpm() > 120 ? "CRITICAL" : "HIGH",
                    "Heart rate is above the recommended threshold"
            ));
        }

        if (reading.getSleepHoursLast24h() < 6) {
            anomalies.add(toAnomaly(
                    baseId + 2,
                    reading,
                    "FATIGUE_SPIKE",
                    reading.getSleepHoursLast24h(),
                    6.0,
                    reading.getSleepHoursLast24h() < 4 ? "CRITICAL" : "HIGH",
                    "Sleep hours during the last 24 hours are below the recommended threshold"
            ));
        }

        if (reading.getSelfReportedFatigueLevel() >= 4) {
            anomalies.add(toAnomaly(
                    baseId + 3,
                    reading,
                    "FATIGUE_SPIKE",
                    reading.getSelfReportedFatigueLevel().doubleValue(),
                    4.0,
                    reading.getSelfReportedFatigueLevel() == 5 ? "CRITICAL" : "HIGH",
                    "Self-reported fatigue level is high"
            ));
        }

        if (anomalies.isEmpty()) {
            anomalies.add(toAnomaly(
                    baseId + 4,
                    reading,
                    "LOW_HRV",
                    60.0,
                    50.0,
                    "LOW",
                    "No critical anomaly detected"
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
                "OPEN",
                type,
                value,
                threshold,
                message,
                reading.getRecordedAt(),
                null,
                null
        );
    }

    private String firstNonBlank(String... values) {
        for (var value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return "";
    }
}