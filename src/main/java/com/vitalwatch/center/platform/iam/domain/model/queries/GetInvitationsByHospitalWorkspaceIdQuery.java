package com.vitalwatch.center.platform.iam.domain.model.queries;

/**
 * Query to get all invitations sent from a hospital workspace.
 */
public record GetInvitationsByHospitalWorkspaceIdQuery(Long hospitalWorkspaceId) {
}