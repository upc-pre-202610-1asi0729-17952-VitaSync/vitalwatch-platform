package com.vitalwatch.center.platform.frontendcompat.interfaces.rest;

import com.vitalwatch.center.platform.audit.domain.model.aggregates.AuditLog;
import com.vitalwatch.center.platform.audit.domain.model.aggregates.ComplianceRecord;
import com.vitalwatch.center.platform.audit.domain.model.commands.RecordAuditLogCommand;
import com.vitalwatch.center.platform.audit.domain.model.commands.RecordComplianceRecordCommand;
import com.vitalwatch.center.platform.audit.domain.model.commands.ReviewComplianceRecordCommand;
import com.vitalwatch.center.platform.audit.domain.model.enums.AuditActionType;
import com.vitalwatch.center.platform.audit.domain.model.enums.AuditResourceType;
import com.vitalwatch.center.platform.audit.domain.model.enums.AuditSeverity;
import com.vitalwatch.center.platform.audit.domain.model.enums.ComplianceStatus;
import com.vitalwatch.center.platform.audit.domain.repositories.AuditLogRepository;
import com.vitalwatch.center.platform.audit.domain.repositories.ComplianceRecordRepository;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.CreateFrontendAuditLogResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.CreateFrontendComplianceRecordResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendAuditLogResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendComplianceRecordResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.ReviewFrontendComplianceRecordResource;
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
 * Frontend compatibility controller for audit and compliance endpoints expected by Angular.
 */
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Frontend API - Audit", description = "Audit and compliance endpoints compatible with the Angular frontend")
public class AuditCompatibilityController {

    private final AuditLogRepository auditLogRepository;
    private final ComplianceRecordRepository complianceRecordRepository;

    public AuditCompatibilityController(
            AuditLogRepository auditLogRepository,
            ComplianceRecordRepository complianceRecordRepository
    ) {
        this.auditLogRepository = auditLogRepository;
        this.complianceRecordRepository = complianceRecordRepository;
    }

    @GetMapping("/auditLogs")
    @Operation(summary = "Get frontend-compatible audit logs")
    public ResponseEntity<List<FrontendAuditLogResource>> getAuditLogs(
            @RequestParam(required = false) @Positive Long organizationId,
            @RequestParam(required = false) @Positive Long hospitalWorkspaceId,
            @RequestParam(required = false) @Positive Long actorUserAccountId,
            @RequestParam(required = false) @Positive Long actorUserId,
            @RequestParam(required = false) @Positive Long userId
    ) {
        var workspaceId = organizationId != null ? organizationId : hospitalWorkspaceId;
        var actorId = actorUserAccountId != null
                ? actorUserAccountId
                : actorUserId != null
                ? actorUserId
                : userId;

        List<AuditLog> logs;

        if (actorId != null) {
            logs = auditLogRepository.findAllByActorUserAccountId(actorId);
        } else if (workspaceId != null) {
            logs = auditLogRepository.findAllByHospitalWorkspaceId(workspaceId);
        } else {
            logs = List.of();
        }

        var resources = logs.stream()
                .map(this::toAuditLogResource)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/auditLogs/{auditLogId}")
    @Operation(summary = "Get frontend-compatible audit log by id")
    public ResponseEntity<FrontendAuditLogResource> getAuditLogById(
            @PathVariable @Positive Long auditLogId
    ) {
        return auditLogRepository.findById(auditLogId)
                .map(this::toAuditLogResource)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/auditLogs")
    @Operation(summary = "Create frontend-compatible audit log")
    public ResponseEntity<FrontendAuditLogResource> createAuditLog(
            @Valid @RequestBody CreateFrontendAuditLogResource resource
    ) {
        var workspaceId = resource.organizationId() != null
                ? resource.organizationId()
                : resource.hospitalWorkspaceId();

        var actorId = resource.actorUserAccountId() != null
                ? resource.actorUserAccountId()
                : resource.userId();

        if (workspaceId == null || workspaceId <= 0 ||
                actorId == null || actorId <= 0 ||
                resource.resourceId() == null || resource.resourceId() <= 0) {
            return ResponseEntity.badRequest().build();
        }

        try {
            var auditLog = new AuditLog(
                    new RecordAuditLogCommand(
                            workspaceId,
                            actorId,
                            resolveActionType(resource.actionType()),
                            resolveResourceType(resource.resourceType()),
                            resource.resourceId(),
                            resolveSeverity(resource.severity()),
                            firstNonBlank(resource.description(), "Frontend-compatible audit log recorded."),
                            firstNonBlank(resource.ipAddress(), "127.0.0.1")
                    )
            );

            var savedAuditLog = auditLogRepository.save(auditLog);
            return ResponseEntity.status(HttpStatus.CREATED).body(toAuditLogResource(savedAuditLog));

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/complianceRecords")
    @Operation(summary = "Get frontend-compatible compliance records")
    public ResponseEntity<List<FrontendComplianceRecordResource>> getComplianceRecords(
            @RequestParam(required = false) @Positive Long organizationId,
            @RequestParam(required = false) @Positive Long hospitalWorkspaceId
    ) {
        var workspaceId = organizationId != null ? organizationId : hospitalWorkspaceId;

        if (workspaceId == null) {
            return ResponseEntity.ok(List.of());
        }

        var resources = complianceRecordRepository.findAllByHospitalWorkspaceId(workspaceId)
                .stream()
                .map(this::toComplianceRecordResource)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/complianceRecords/{complianceRecordId}")
    @Operation(summary = "Get frontend-compatible compliance record by id")
    public ResponseEntity<FrontendComplianceRecordResource> getComplianceRecordById(
            @PathVariable @Positive Long complianceRecordId
    ) {
        return complianceRecordRepository.findById(complianceRecordId)
                .map(this::toComplianceRecordResource)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/complianceRecords")
    @Operation(summary = "Create frontend-compatible compliance record")
    public ResponseEntity<FrontendComplianceRecordResource> createComplianceRecord(
            @Valid @RequestBody CreateFrontendComplianceRecordResource resource
    ) {
        var workspaceId = resource.organizationId() != null
                ? resource.organizationId()
                : resource.hospitalWorkspaceId();

        if (workspaceId == null || workspaceId <= 0 ||
                resource.resourceId() == null || resource.resourceId() <= 0) {
            return ResponseEntity.badRequest().build();
        }

        try {
            var complianceRecord = new ComplianceRecord(
                    new RecordComplianceRecordCommand(
                            workspaceId,
                            resolveResourceType(resource.resourceType()),
                            resource.resourceId(),
                            resolveComplianceStatus(resource.status()),
                            firstNonBlank(resource.description(), "Frontend-compatible compliance record created.")
                    )
            );

            var savedComplianceRecord = complianceRecordRepository.save(complianceRecord);
            return ResponseEntity.status(HttpStatus.CREATED).body(toComplianceRecordResource(savedComplianceRecord));

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/complianceRecords/{complianceRecordId}/review")
    @Operation(summary = "Review frontend-compatible compliance record")
    public ResponseEntity<FrontendComplianceRecordResource> reviewComplianceRecord(
            @PathVariable @Positive Long complianceRecordId,
            @Valid @RequestBody ReviewFrontendComplianceRecordResource resource
    ) {
        var reviewerId = resolveReviewerId(resource);

        if (reviewerId == null || reviewerId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        var complianceRecord = complianceRecordRepository.findById(complianceRecordId);

        if (complianceRecord.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            var recordToReview = complianceRecord.get();

            recordToReview.review(
                    new ReviewComplianceRecordCommand(
                            complianceRecordId,
                            reviewerId,
                            firstNonBlank(resource.reviewNotes(), resource.notes(), "Compliance record reviewed.")
                    )
            );

            var savedComplianceRecord = complianceRecordRepository.save(recordToReview);
            return ResponseEntity.ok(toComplianceRecordResource(savedComplianceRecord));

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    private FrontendAuditLogResource toAuditLogResource(AuditLog entity) {
        return new FrontendAuditLogResource(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getHospitalWorkspaceId(),
                entity.getActorUserAccountId(),
                entity.getActorUserAccountId(),
                entity.getActorUserAccountId(),
                entity.getActionType().name(),
                entity.getActionType().name(),
                entity.getResourceType().name(),
                entity.getResourceId(),
                entity.getSeverity().name(),
                entity.getDescription(),
                entity.getIpAddress(),
                entity.getOccurredAt(),
                entity.getOccurredAt()
        );
    }

    private FrontendComplianceRecordResource toComplianceRecordResource(ComplianceRecord entity) {
        return new FrontendComplianceRecordResource(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getHospitalWorkspaceId(),
                entity.getResourceType().name(),
                entity.getResourceId(),
                entity.getStatus().name(),
                entity.getDescription(),
                entity.getReviewedByUserAccountId(),
                entity.getReviewedByUserAccountId(),
                entity.getReviewNotes(),
                entity.getRecordedAt(),
                entity.getRecordedAt(),
                entity.getReviewedAt()
        );
    }

    private AuditActionType resolveActionType(String value) {
        if (value == null || value.isBlank()) return AuditActionType.UPDATE;
        try {
            return AuditActionType.valueOf(value.trim().toUpperCase().replace("-", "_"));
        } catch (IllegalArgumentException exception) {
            return AuditActionType.UPDATE;
        }
    }

    private AuditResourceType resolveResourceType(String value) {
        if (value == null || value.isBlank()) return AuditResourceType.PROFILE;
        try {
            return AuditResourceType.valueOf(value.trim().toUpperCase().replace("-", "_"));
        } catch (IllegalArgumentException exception) {
            return AuditResourceType.PROFILE;
        }
    }

    private AuditSeverity resolveSeverity(String value) {
        if (value == null || value.isBlank()) return AuditSeverity.MEDIUM;
        try {
            return AuditSeverity.valueOf(value.trim().toUpperCase().replace("-", "_"));
        } catch (IllegalArgumentException exception) {
            return AuditSeverity.MEDIUM;
        }
    }

    private ComplianceStatus resolveComplianceStatus(String value) {
        if (value == null || value.isBlank()) return ComplianceStatus.PENDING_REVIEW;

        var normalized = value.trim().toUpperCase().replace("-", "_");

        if ("PENDING".equals(normalized)) {
            return ComplianceStatus.PENDING_REVIEW;
        }

        try {
            return ComplianceStatus.valueOf(normalized);
        } catch (IllegalArgumentException exception) {
            return ComplianceStatus.PENDING_REVIEW;
        }
    }

    private Long resolveReviewerId(ReviewFrontendComplianceRecordResource resource) {
        if (resource.reviewedByUserAccountId() != null) return resource.reviewedByUserAccountId();
        if (resource.reviewedByUserId() != null) return resource.reviewedByUserId();
        if (resource.userAccountId() != null) return resource.userAccountId();
        return resource.userId();
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