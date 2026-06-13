package com.vitalwatch.center.platform.clinicalrisk.domain.model.queries;

/**
 * Query to get vital sign readings by user account.
 */
public record GetVitalSignReadingsByUserAccountIdQuery(Long userAccountId) {
}