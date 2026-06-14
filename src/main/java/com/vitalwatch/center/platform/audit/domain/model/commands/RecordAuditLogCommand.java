package com.vitalwatch.center.platform.audit.domain.model.commands;

import com.vitalwatch.center.platform.audit.domain.model.enums.AuditActionType;
import com.vitalwatch.center.platform.audit.domain.model.enums.AuditResourceType;
import com.vitalwatch.center.platform.audit.domain.model.enums.AuditSeverity;

/**
 * Command used to record an audit log.
 */
public record RecordAuditLogCommand(
        Long hospitalWorkspaceId,
        Long actorUserAccountId,
        AuditActionType actionType,
        AuditResourceType resourceType,
        Long resourceId,
        AuditSeverity severity,
        String description,
        String ipAddress
) {
}