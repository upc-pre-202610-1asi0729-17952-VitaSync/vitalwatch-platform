package com.vitalwatch.center.platform.audit.interfaces.rest.transform;

import com.vitalwatch.center.platform.audit.domain.model.commands.RecordAuditLogCommand;
import com.vitalwatch.center.platform.audit.interfaces.rest.resources.RecordAuditLogResource;

/**
 * Assembler to convert RecordAuditLogResource into RecordAuditLogCommand.
 */
public final class RecordAuditLogCommandFromResourceAssembler {

    private RecordAuditLogCommandFromResourceAssembler() {
    }

    public static RecordAuditLogCommand toCommandFromResource(RecordAuditLogResource resource) {
        return new RecordAuditLogCommand(
                resource.hospitalWorkspaceId(),
                resource.actorUserAccountId(),
                resource.actionType(),
                resource.resourceType(),
                resource.resourceId(),
                resource.severity(),
                resource.description(),
                resource.ipAddress()
        );
    }
}