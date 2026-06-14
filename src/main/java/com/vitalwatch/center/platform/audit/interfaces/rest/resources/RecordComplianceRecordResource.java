package com.vitalwatch.center.platform.audit.interfaces.rest.resources;

import com.vitalwatch.center.platform.audit.domain.model.enums.AuditResourceType;
import com.vitalwatch.center.platform.audit.domain.model.enums.ComplianceStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Resource used to record a compliance record.
 */
@Schema(name = "RecordComplianceRecordRequest", description = "Request payload for recording a compliance record")
public record RecordComplianceRecordResource(

        @NotNull
        @Positive
        @Schema(description = "Hospital workspace id", example = "1")
        Long hospitalWorkspaceId,

        @NotNull
        @Schema(description = "Audited resource type", example = "RECOVERY_PLAN")
        AuditResourceType resourceType,

        @NotNull
        @Positive
        @Schema(description = "Audited resource id", example = "1")
        Long resourceId,

        @NotNull
        @Schema(description = "Compliance status", example = "PENDING_REVIEW")
        ComplianceStatus status,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Compliance description", example = "Recovery plan requires supervisor compliance review.")
        String description
) {
}