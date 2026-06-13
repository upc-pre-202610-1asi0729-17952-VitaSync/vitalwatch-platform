package com.vitalwatch.center.platform.clinicalrisk.domain.model.commands;

/**
 * Command used to escalate a high or critical risk assessment.
 */
public record EscalateClinicalRiskAssessmentCommand(
        Long clinicalRiskAssessmentId
) {
}