package com.vitalwatch.center.platform.audit.interfaces.rest;

import com.vitalwatch.center.platform.audit.application.commandservices.AuditCommandService;
import com.vitalwatch.center.platform.audit.application.queryservices.AuditQueryService;
import com.vitalwatch.center.platform.audit.domain.model.queries.GetAuditLogByIdQuery;
import com.vitalwatch.center.platform.audit.domain.model.queries.GetAuditLogsByActorUserAccountIdQuery;
import com.vitalwatch.center.platform.audit.domain.model.queries.GetAuditLogsByHospitalWorkspaceIdQuery;
import com.vitalwatch.center.platform.audit.interfaces.rest.resources.AuditLogResource;
import com.vitalwatch.center.platform.audit.interfaces.rest.resources.RecordAuditLogResource;
import com.vitalwatch.center.platform.audit.interfaces.rest.transform.AuditLogResourceFromEntityAssembler;
import com.vitalwatch.center.platform.audit.interfaces.rest.transform.RecordAuditLogCommandFromResourceAssembler;
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
 * REST controller for audit logs.
 */
@RestController
@RequestMapping(value = "/api/v1/audit-logs", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Audit Logs", description = "Audit trail management endpoints")
public class AuditLogsController {

    private final AuditCommandService auditCommandService;
    private final AuditQueryService auditQueryService;

    public AuditLogsController(
            AuditCommandService auditCommandService,
            AuditQueryService auditQueryService
    ) {
        this.auditCommandService = auditCommandService;
        this.auditQueryService = auditQueryService;
    }

    @PostMapping
    @Operation(summary = "Record audit log", description = "Records an auditable system action.")
    public ResponseEntity<?> recordAuditLog(@Valid @RequestBody RecordAuditLogResource resource) {
        var command = RecordAuditLogCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = auditCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                AuditLogResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{auditLogId}")
    @Operation(summary = "Get audit log by id", description = "Retrieves an audit log by id.")
    public ResponseEntity<?> getAuditLogById(@PathVariable @Positive Long auditLogId) {
        var auditLog = auditQueryService.handle(new GetAuditLogByIdQuery(auditLogId));

        if (auditLog.isEmpty()) {
            var error = ApplicationError.notFound("AuditLog", auditLogId.toString());
            return ErrorResponseAssembler.toErrorResponseFromApplicationError(error);
        }

        var resource = AuditLogResourceFromEntityAssembler.toResourceFromEntity(auditLog.get());
        return ResponseEntity.ok(resource);
    }

    @GetMapping(params = "hospitalWorkspaceId")
    @Operation(summary = "Get audit logs by hospital workspace", description = "Retrieves audit logs for a hospital workspace.")
    public ResponseEntity<List<AuditLogResource>> getAuditLogsByHospitalWorkspaceId(
            @RequestParam @Positive Long hospitalWorkspaceId
    ) {
        var auditLogs = auditQueryService.handle(new GetAuditLogsByHospitalWorkspaceIdQuery(hospitalWorkspaceId));

        var resources = auditLogs.stream()
                .map(AuditLogResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping(value = "/actor", params = "actorUserAccountId")
    @Operation(summary = "Get audit logs by actor", description = "Retrieves audit logs performed by a user account.")
    public ResponseEntity<List<AuditLogResource>> getAuditLogsByActorUserAccountId(
            @RequestParam @Positive Long actorUserAccountId
    ) {
        var auditLogs = auditQueryService.handle(new GetAuditLogsByActorUserAccountIdQuery(actorUserAccountId));

        var resources = auditLogs.stream()
                .map(AuditLogResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }
}