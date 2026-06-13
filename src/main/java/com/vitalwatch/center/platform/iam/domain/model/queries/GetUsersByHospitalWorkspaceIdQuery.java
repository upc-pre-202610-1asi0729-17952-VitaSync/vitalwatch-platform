package com.vitalwatch.center.platform.iam.domain.model.queries;

/**
 * Query to get all users assigned to a hospital workspace.
 */
public record GetUsersByHospitalWorkspaceIdQuery(Long hospitalWorkspaceId) {
}