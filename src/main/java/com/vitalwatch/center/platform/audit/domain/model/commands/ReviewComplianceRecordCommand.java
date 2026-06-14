package com.vitalwatch.center.platform.audit.domain.model.commands;

/**
 * Command used to review a compliance record.
 */
public record ReviewComplianceRecordCommand(
        Long complianceRecordId,
        Long reviewedByUserAccountId,
        String reviewNotes
) {
}