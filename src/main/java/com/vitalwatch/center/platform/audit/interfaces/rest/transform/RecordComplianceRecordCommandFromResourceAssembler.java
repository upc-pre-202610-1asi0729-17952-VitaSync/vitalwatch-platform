package com.vitalwatch.center.platform.audit.interfaces.rest.transform;

import com.vitalwatch.center.platform.audit.domain.model.commands.RecordComplianceRecordCommand;
import com.vitalwatch.center.platform.audit.interfaces.rest.resources.RecordComplianceRecordResource;

/**
 * Assembler to convert RecordComplianceRecordResource into RecordComplianceRecordCommand.
 */
public final class RecordComplianceRecordCommandFromResourceAssembler {

    private RecordComplianceRecordCommandFromResourceAssembler() {
    }

    public static RecordComplianceRecordCommand toCommandFromResource(RecordComplianceRecordResource resource) {
        return new RecordComplianceRecordCommand(
                resource.hospitalWorkspaceId(),
                resource.resourceType(),
                resource.resourceId(),
                resource.status(),
                resource.description()
        );
    }
}