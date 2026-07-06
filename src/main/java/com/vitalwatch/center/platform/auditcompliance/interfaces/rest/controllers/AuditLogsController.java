package com.vitalwatch.center.platform.auditcompliance.interfaces.rest.controllers;

import com.vitalwatch.center.platform.auditcompliance.domain.model.enums.AuditLogSeverity;
import com.vitalwatch.center.platform.auditcompliance.domain.model.enums.AuditLogType;
import com.vitalwatch.center.platform.auditcompliance.infrastructure.persistence.jpa.entities.AuditLogJpaEntity;
import com.vitalwatch.center.platform.auditcompliance.infrastructure.persistence.jpa.repositories.AuditLogJpaRepository;
import com.vitalwatch.center.platform.auditcompliance.interfaces.rest.resources.AuditLogResource;
import com.vitalwatch.center.platform.auditcompliance.interfaces.rest.resources.CreateAuditLogResource;
import com.vitalwatch.center.platform.auditcompliance.interfaces.rest.transform.AuditLogResourceFromEntityAssembler;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.OrganizationJpaRepository;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import com.vitalwatch.center.platform.shared.application.i18n.MessageResolver;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

/**
 * REST controller for audit logs.
 */
@RestController
@RequestMapping("/auditLogs")
@Tag(name = "Audit Logs", description = "Audit compliance log endpoints")
public class AuditLogsController {

    private final AuditLogJpaRepository auditLogRepository;
    private final OrganizationJpaRepository organizationRepository;
    private final UserJpaRepository userRepository;
    private final MessageResolver messageResolver;

    public AuditLogsController(
            AuditLogJpaRepository auditLogRepository,
            OrganizationJpaRepository organizationRepository,
            UserJpaRepository userRepository,
            MessageResolver messageResolver
    ) {
        this.auditLogRepository = auditLogRepository;
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
        this.messageResolver = messageResolver;
    }

    @GetMapping
    @Operation(summary = "Get all audit logs or filter them")
    public ResponseEntity<List<AuditLogResource>> getAuditLogs(
            @RequestParam(required = false) Long organizationId,
            @RequestParam(required = false) Long actorUserId,
            @RequestParam(required = false) AuditLogType type,
            @RequestParam(required = false) AuditLogSeverity severity
    ) {
        var logs = organizationId != null && actorUserId != null
                ? auditLogRepository.findByOrganizationIdAndActorUserIdOrderByCreatedAtDesc(
                organizationId,
                actorUserId
        )
                : organizationId != null && type != null
                  ? auditLogRepository.findByOrganizationIdAndTypeOrderByCreatedAtDesc(
                organizationId,
                type
        )
                  : organizationId != null && severity != null
                    ? auditLogRepository.findByOrganizationIdAndSeverityOrderByCreatedAtDesc(
                organizationId,
                severity
        )
                    : organizationId != null
                      ? auditLogRepository.findByOrganizationIdOrderByCreatedAtDesc(organizationId)
                      : auditLogRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(AuditLogJpaEntity::getCreatedAt).reversed())
                .toList();

        var resources = logs.stream()
                .map(AuditLogResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{auditLogId}")
    @Operation(summary = "Get audit log by id")
    public ResponseEntity<?> getAuditLogById(@PathVariable Long auditLogId) {
        var auditLog = auditLogRepository.findById(auditLogId);

        if (auditLog.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("audit.auditLog.notFound")
                    )
            );
        }

        return ResponseEntity.ok(
                AuditLogResourceFromEntityAssembler.toResourceFromEntity(auditLog.get())
        );
    }

    @PostMapping
    @Operation(summary = "Create audit log")
    public ResponseEntity<?> createAuditLog(
            @Valid @RequestBody CreateAuditLogResource resource
    ) {
        if (!organizationRepository.existsById(resource.organizationId())) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("iam.organization.notFound")
                    )
            );
        }

        if (resource.actorUserId() != null && !userRepository.existsById(resource.actorUserId())) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("iam.user.notFound")
                    )
            );
        }

        var auditLog = new AuditLogJpaEntity(
                resource.organizationId(),
                resource.actorUserId(),
                resource.type(),
                resource.severity(),
                resource.resourceType(),
                resource.resourceId(),
                resource.description(),
                resource.createdAt()
        );

        var savedAuditLog = auditLogRepository.save(auditLog);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                AuditLogResourceFromEntityAssembler.toResourceFromEntity(savedAuditLog)
        );
    }
}