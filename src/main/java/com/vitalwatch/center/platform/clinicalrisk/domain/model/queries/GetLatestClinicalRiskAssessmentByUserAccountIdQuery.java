package com.vitalwatch.center.platform.clinicalrisk.domain.model.queries;

/**
 * Query to get the latest clinical risk assessment for a user account.
 */
public record GetLatestClinicalRiskAssessmentByUserAccountIdQuery(Long userAccountId) {
}