package com.vitalwatch.center.platform.clinicalrisk.domain.model.commands;

/**
 * Command used to mark a risk assessment as reviewed.
 */
public record ReviewClinicalRiskAssessmentCommand(
        Long clinicalRiskAssessmentId
) {
}