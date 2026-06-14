package com.vitalwatch.center.platform.audit.interfaces.rest;

import com.vitalwatch.center.platform.audit.application.commandservices.AuditCommandService;
import com.vitalwatch.center.platform.audit.application.queryservices.AuditQueryService;
import com.vitalwatch.center.platform.audit.domain.model.queries.GetComplianceRecordByIdQuery;
import com.vitalwatch.center.platform.audit.domain.model.queries.GetComplianceRecordsByHospitalWorkspaceIdQuery;
import com.vitalwatch.center.platform.audit.interfaces.rest.resources.ComplianceRecordResource;
import com.vitalwatch.center.platform.audit.interfaces.rest.resources.RecordComplianceRecordResource;
import com.vitalwatch.center.platform.audit.interfaces.rest.resources.ReviewComplianceRecordResource;
import com.vitalwatch.center.platform.audit.interfaces.rest.transform.ComplianceRecordResourceFromEntityAssembler;
import com.vitalwatch.center.platform.audit.interfaces.rest.transform.RecordComplianceRecordCommandFromResourceAssembler;
import com.vitalwatch.center.platform.audit.interfaces.rest.transform.ReviewComplianceRecordCommandFromResourceAssembler;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
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
 * REST controller for compliance records.
 */
@RestController
@RequestMapping(value = "/api/v1/compliance-records", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Compliance Records", description = "Compliance review management endpoints")
public class ComplianceRecordsController {

    private final AuditCommandService auditCommandService;
    private final AuditQueryService auditQueryService;

    public ComplianceRecordsController(
            AuditCommandService auditCommandService,
            AuditQueryService auditQueryService
    ) {
        this.auditCommandService = auditCommandService;
        this.auditQueryService = auditQueryService;
    }

    @PostMapping
    @Operation(summary = "Record compliance record", description = "Records a compliance check for a system resource.")
    public ResponseEntity<?> recordComplianceRecord(@Valid @RequestBody RecordComplianceRecordResource resource) {
        var command = RecordComplianceRecordCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = auditCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                ComplianceRecordResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{complianceRecordId}")
    @Operation(summary = "Get compliance record by id", description = "Retrieves a compliance record by id.")
    public ResponseEntity<?> getComplianceRecordById(@PathVariable @Positive Long complianceRecordId) {
        var complianceRecord = auditQueryService.handle(new GetComplianceRecordByIdQuery(complianceRecordId));

        if (complianceRecord.isEmpty()) {
            var error = ApplicationError.notFound("ComplianceRecord", complianceRecordId.toString());
            return ErrorResponseAssembler.toErrorResponseFromApplicationError(error);
        }

        var resource = ComplianceRecordResourceFromEntityAssembler.toResourceFromEntity(complianceRecord.get());
        return ResponseEntity.ok(resource);
    }

    @GetMapping(params = "hospitalWorkspaceId")
    @Operation(summary = "Get compliance records by hospital workspace", description = "Retrieves compliance records for a hospital workspace.")
    public ResponseEntity<List<ComplianceRecordResource>> getComplianceRecordsByHospitalWorkspaceId(
            @RequestParam @Positive Long hospitalWorkspaceId
    ) {
        var complianceRecords = auditQueryService.handle(
                new GetComplianceRecordsByHospitalWorkspaceIdQuery(hospitalWorkspaceId)
        );

        var resources = complianceRecords.stream()
                .map(ComplianceRecordResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @PatchMapping("/{complianceRecordId}/review")
    @Operation(summary = "Review compliance record", description = "Marks a compliance record as reviewed.")
    public ResponseEntity<?> reviewComplianceRecord(
            @PathVariable @Positive Long complianceRecordId,
            @Valid @RequestBody ReviewComplianceRecordResource resource
    ) {
        var command = ReviewComplianceRecordCommandFromResourceAssembler.toCommandFromResource(
                complianceRecordId,
                resource
        );
        var result = auditCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                ComplianceRecordResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK
        );
    }
}