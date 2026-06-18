package com.vitalwatch.center.platform.frontendcompat.interfaces.rest;

import com.vitalwatch.center.platform.clinicalrisk.domain.model.aggregates.ClinicalRiskAssessment;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.aggregates.VitalSignReading;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.commands.CreateClinicalRiskAssessmentCommand;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.commands.RegisterVitalSignReadingCommand;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.enums.VitalSignSource;
import com.vitalwatch.center.platform.clinicalrisk.domain.repositories.ClinicalRiskAssessmentRepository;
import com.vitalwatch.center.platform.clinicalrisk.domain.repositories.VitalSignReadingRepository;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.CreateFrontendRiskAssessmentResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.CreateFrontendVitalSignReadingResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendRiskAssessmentResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendVitalSignReadingResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.transform.FrontendRiskAssessmentResourceFromEntityAssembler;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.transform.FrontendVitalSignReadingResourceFromEntityAssembler;
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
 * Frontend compatibility controller for clinical risk endpoints expected by Angular.
 */
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Frontend API - Clinical Risk", description = "Clinical risk endpoints compatible with the Angular frontend")
public class ClinicalRiskCompatibilityController {

    private final VitalSignReadingRepository vitalSignReadingRepository;
    private final ClinicalRiskAssessmentRepository clinicalRiskAssessmentRepository;

    public ClinicalRiskCompatibilityController(
            VitalSignReadingRepository vitalSignReadingRepository,
            ClinicalRiskAssessmentRepository clinicalRiskAssessmentRepository
    ) {
        this.vitalSignReadingRepository = vitalSignReadingRepository;
        this.clinicalRiskAssessmentRepository = clinicalRiskAssessmentRepository;
    }

    @GetMapping("/vitalSignReadings")
    @Operation(summary = "Get frontend-compatible vital sign readings")
    public ResponseEntity<List<FrontendVitalSignReadingResource>> getVitalSignReadings(
            @RequestParam(required = false) Long organizationId,
            @RequestParam(required = false) Long hospitalWorkspaceId,
            @RequestParam(required = false) Long userAccountId,
            @RequestParam(required = false) Long userId
    ) {
        var selectedUserId = userAccountId != null ? userAccountId : userId;
        var selectedWorkspaceId = organizationId != null ? organizationId : hospitalWorkspaceId;

        List<VitalSignReading> readings;

        if (selectedUserId != null) {
            readings = vitalSignReadingRepository.findAllByUserAccountId(selectedUserId);
        } else if (selectedWorkspaceId != null) {
            readings = vitalSignReadingRepository.findAllByHospitalWorkspaceId(selectedWorkspaceId);
        } else {
            readings = List.of();
        }

        var resources = readings.stream()
                .map(FrontendVitalSignReadingResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/vitalSignReadings/{vitalSignReadingId}")
    @Operation(summary = "Get frontend-compatible vital sign reading by id")
    public ResponseEntity<FrontendVitalSignReadingResource> getVitalSignReadingById(
            @PathVariable @Positive Long vitalSignReadingId
    ) {
        return vitalSignReadingRepository.findById(vitalSignReadingId)
                .map(FrontendVitalSignReadingResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/vitalSignReadings")
    @Operation(summary = "Create frontend-compatible vital sign reading")
    public ResponseEntity<FrontendVitalSignReadingResource> createVitalSignReading(
            @Valid @RequestBody CreateFrontendVitalSignReadingResource resource
    ) {
        var workspaceId = resource.organizationId() != null
                ? resource.organizationId()
                : resource.hospitalWorkspaceId();

        var selectedUserId = resource.userAccountId() != null
                ? resource.userAccountId()
                : resource.userId();

        if (workspaceId == null || workspaceId <= 0 || selectedUserId == null || selectedUserId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        var command = new RegisterVitalSignReadingCommand(
                workspaceId,
                selectedUserId,
                resolveHeartRate(resource),
                resolveDouble(resource.sleepHoursLast24h(), 7.0, 0.0, 24.0),
                resolveDouble(resource.shiftHoursLast24h(), 8.0, 0.0, 24.0),
                resolveInteger(resource.selfReportedFatigueLevel(), 3, 1, 5),
                resolveSource(resource.source())
        );

        var reading = new VitalSignReading(command);
        var savedReading = vitalSignReadingRepository.save(reading);
        var response = FrontendVitalSignReadingResourceFromEntityAssembler.toResourceFromEntity(savedReading);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/riskAssessments")
    @Operation(summary = "Get frontend-compatible risk assessments")
    public ResponseEntity<List<FrontendRiskAssessmentResource>> getRiskAssessments(
            @RequestParam(required = false) Long organizationId,
            @RequestParam(required = false) Long hospitalWorkspaceId,
            @RequestParam(required = false) Long userAccountId,
            @RequestParam(required = false) Long userId
    ) {
        var selectedUserId = userAccountId != null ? userAccountId : userId;
        var selectedWorkspaceId = organizationId != null ? organizationId : hospitalWorkspaceId;

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

        var resources = assessments.stream()
                .map(FrontendRiskAssessmentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/riskAssessments/latest")
    @Operation(summary = "Get latest frontend-compatible risk assessment")
    public ResponseEntity<FrontendRiskAssessmentResource> getLatestRiskAssessment(
            @RequestParam @Positive Long userAccountId
    ) {
        return clinicalRiskAssessmentRepository.findLatestByUserAccountId(userAccountId)
                .map(FrontendRiskAssessmentResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/riskAssessments/{riskAssessmentId}")
    @Operation(summary = "Get frontend-compatible risk assessment by id")
    public ResponseEntity<FrontendRiskAssessmentResource> getRiskAssessmentById(
            @PathVariable @Positive Long riskAssessmentId
    ) {
        return clinicalRiskAssessmentRepository.findById(riskAssessmentId)
                .map(FrontendRiskAssessmentResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/riskAssessments")
    @Operation(summary = "Create frontend-compatible risk assessment")
    public ResponseEntity<FrontendRiskAssessmentResource> createRiskAssessment(
            @Valid @RequestBody CreateFrontendRiskAssessmentResource resource
    ) {
        var workspaceId = resource.organizationId() != null
                ? resource.organizationId()
                : resource.hospitalWorkspaceId();

        var selectedUserId = resource.userAccountId() != null
                ? resource.userAccountId()
                : resource.userId();

        if (workspaceId == null || workspaceId <= 0 ||
                selectedUserId == null || selectedUserId <= 0 ||
                resource.vitalSignReadingId() == null || resource.vitalSignReadingId() <= 0) {
            return ResponseEntity.badRequest().build();
        }

        var reading = vitalSignReadingRepository.findById(resource.vitalSignReadingId());

        if (reading.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (!reading.get().getHospitalWorkspaceId().equals(workspaceId) ||
                !reading.get().getUserAccountId().equals(selectedUserId)) {
            return ResponseEntity.badRequest().build();
        }

        var command = new CreateClinicalRiskAssessmentCommand(
                workspaceId,
                selectedUserId,
                resource.vitalSignReadingId()
        );

        var assessment = new ClinicalRiskAssessment(command, reading.get());
        var savedAssessment = clinicalRiskAssessmentRepository.save(assessment);
        var response = FrontendRiskAssessmentResourceFromEntityAssembler.toResourceFromEntity(savedAssessment);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private Integer resolveHeartRate(CreateFrontendVitalSignReadingResource resource) {
        if (resource.heartRateBpm() != null) {
            return resolveInteger(resource.heartRateBpm(), 80, 30, 240);
        }
        if (resource.bpm() != null) {
            return resolveInteger(resource.bpm(), 80, 30, 240);
        }
        if (resource.heartRate() != null) {
            return resolveInteger(resource.heartRate(), 80, 30, 240);
        }
        return 80;
    }

    private Integer resolveInteger(Integer value, Integer defaultValue, Integer min, Integer max) {
        var resolved = value != null ? value : defaultValue;
        return Math.max(min, Math.min(max, resolved));
    }

    private Double resolveDouble(Double value, Double defaultValue, Double min, Double max) {
        var resolved = value != null ? value : defaultValue;
        return Math.max(min, Math.min(max, resolved));
    }

    private VitalSignSource resolveSource(String source) {
        if (source == null || source.isBlank()) {
            return VitalSignSource.MANUAL_ENTRY;
        }

        try {
            return VitalSignSource.valueOf(source.trim().toUpperCase().replace("-", "_"));
        } catch (IllegalArgumentException exception) {
            return VitalSignSource.MANUAL_ENTRY;
        }
    }
}