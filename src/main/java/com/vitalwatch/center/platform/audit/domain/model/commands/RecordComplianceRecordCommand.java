package com.vitalwatch.center.platform.audit.domain.model.commands;

import com.vitalwatch.center.platform.audit.domain.model.enums.AuditResourceType;
import com.vitalwatch.center.platform.audit.domain.model.enums.ComplianceStatus;

/**
 * Command used to record a compliance check.
 */
public record RecordComplianceRecordCommand(
        Long hospitalWorkspaceId,
        AuditResourceType resourceType,
        Long resourceId,
        ComplianceStatus status,
        String description
) {
}