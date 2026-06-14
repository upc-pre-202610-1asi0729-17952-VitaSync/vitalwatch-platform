package com.vitalwatch.center.platform.audit.interfaces.rest.transform;

import com.vitalwatch.center.platform.audit.domain.model.commands.ReviewComplianceRecordCommand;
import com.vitalwatch.center.platform.audit.interfaces.rest.resources.ReviewComplianceRecordResource;

/**
 * Assembler to convert ReviewComplianceRecordResource into ReviewComplianceRecordCommand.
 */
public final class ReviewComplianceRecordCommandFromResourceAssembler {

    private ReviewComplianceRecordCommandFromResourceAssembler() {
    }

    public static ReviewComplianceRecordCommand toCommandFromResource(
            Long complianceRecordId,
            ReviewComplianceRecordResource resource
    ) {
        return new ReviewComplianceRecordCommand(
                complianceRecordId,
                resource.reviewedByUserAccountId(),
                resource.reviewNotes()
        );
    }
}