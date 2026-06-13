package com.vitalwatch.center.platform.incidents.domain.model.queries;

/**
 * Query to get incidents associated with a reported user account.
 */
public record GetIncidentsByReportedUserAccountIdQuery(Long reportedUserAccountId) {
}