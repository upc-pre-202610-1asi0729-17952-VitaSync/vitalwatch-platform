package com.vitalwatch.center.platform.clinicalrisk.domain.model.commands;

/**
 * Command used to create a fatigue and clinical risk assessment.
 */
public record CreateClinicalRiskAssessmentCommand(
        Long hospitalWorkspaceId,
        Long userAccountId,
        Long vitalSignReadingId
) {
}