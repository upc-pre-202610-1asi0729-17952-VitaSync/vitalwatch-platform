package com.vitalwatch.center.platform.audit.interfaces.rest.resources;

import com.vitalwatch.center.platform.audit.domain.model.enums.AuditActionType;
import com.vitalwatch.center.platform.audit.domain.model.enums.AuditResourceType;
import com.vitalwatch.center.platform.audit.domain.model.enums.AuditSeverity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Resource used to record an audit log.
 */
@Schema(name = "RecordAuditLogRequest", description = "Request payload for recording an audit log")
public record RecordAuditLogResource(

        @NotNull
        @Positive
        @Schema(description = "Hospital workspace id", example = "1")
        Long hospitalWorkspaceId,

        @NotNull
        @Positive
        @Schema(description = "Actor user account id", example = "1")
        Long actorUserAccountId,

        @NotNull
        @Schema(description = "Audit action type", example = "INCIDENT_RESOLVED")
        AuditActionType actionType,

        @NotNull
        @Schema(description = "Affected resource type", example = "INCIDENT")
        AuditResourceType resourceType,

        @NotNull
        @Positive
        @Schema(description = "Affected resource id", example = "1")
        Long resourceId,

        @NotNull
        @Schema(description = "Audit severity", example = "HIGH")
        AuditSeverity severity,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Audit description", example = "Incident was resolved after supervisor intervention.")
        String description,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "IP address", example = "127.0.0.1")
        String ipAddress
) {
}