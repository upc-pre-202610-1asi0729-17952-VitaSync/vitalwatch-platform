package com.vitalwatch.center.platform.audit.interfaces.rest.resources;

import com.vitalwatch.center.platform.audit.domain.model.enums.AuditResourceType;
import com.vitalwatch.center.platform.audit.domain.model.enums.ComplianceStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Resource used to expose compliance record data.
 */
@Schema(name = "ComplianceRecordResponse", description = "Compliance record information response")
public record ComplianceRecordResource(

        @Schema(description = "Compliance record id", example = "1")
        Long id,

        @Schema(description = "Hospital workspace id", example = "1")
        Long hospitalWorkspaceId,

        @Schema(description = "Audited resource type", example = "RECOVERY_PLAN")
        AuditResourceType resourceType,

        @Schema(description = "Audited resource id", example = "1")
        Long resourceId,

        @Schema(description = "Compliance status", example = "PENDING_REVIEW")
        ComplianceStatus status,

        @Schema(description = "Compliance description")
        String description,

        @Schema(description = "User account id that reviewed the record")
        Long reviewedByUserAccountId,

        @Schema(description = "Review notes")
        String reviewNotes,

        @Schema(description = "Record creation date")
        Instant recordedAt,

        @Schema(description = "Review date")
        Instant reviewedAt
) {
}