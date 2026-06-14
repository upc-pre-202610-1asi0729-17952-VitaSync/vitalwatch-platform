package com.vitalwatch.center.platform.audit.interfaces.rest.resources;

import com.vitalwatch.center.platform.audit.domain.model.enums.AuditActionType;
import com.vitalwatch.center.platform.audit.domain.model.enums.AuditResourceType;
import com.vitalwatch.center.platform.audit.domain.model.enums.AuditSeverity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Resource used to expose audit log data.
 */
@Schema(name = "AuditLogResponse", description = "Audit log information response")
public record AuditLogResource(

        @Schema(description = "Audit log id", example = "1")
        Long id,

        @Schema(description = "Hospital workspace id", example = "1")
        Long hospitalWorkspaceId,

        @Schema(description = "Actor user account id", example = "1")
        Long actorUserAccountId,

        @Schema(description = "Audit action type", example = "INCIDENT_RESOLVED")
        AuditActionType actionType,

        @Schema(description = "Affected resource type", example = "INCIDENT")
        AuditResourceType resourceType,

        @Schema(description = "Affected resource id", example = "1")
        Long resourceId,

        @Schema(description = "Audit severity", example = "HIGH")
        AuditSeverity severity,

        @Schema(description = "Audit description")
        String description,

        @Schema(description = "IP address")
        String ipAddress,

        @Schema(description = "Date when the action occurred")
        Instant occurredAt
) {
}