package com.vitalwatch.center.platform.auditcompliance.interfaces.rest.resources;

import com.vitalwatch.center.platform.auditcompliance.domain.model.enums.AuditLogSeverity;
import com.vitalwatch.center.platform.auditcompliance.domain.model.enums.AuditLogType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

/**
 * Resource used to create an audit log.
 */
public record CreateAuditLogResource(
        @NotNull
        Long organizationId,

        Long actorUserId,

        @NotNull
        AuditLogType type,

        @NotNull
        AuditLogSeverity severity,

        @NotBlank
        @Size(max = 80)
        String resourceType,

        Long resourceId,

        @NotBlank
        @Size(max = 1000)
        String description,

        OffsetDateTime createdAt
) {
}