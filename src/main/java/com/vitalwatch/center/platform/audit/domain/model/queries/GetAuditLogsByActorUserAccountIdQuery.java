package com.vitalwatch.center.platform.audit.domain.model.queries;

/**
 * Query to get audit logs by actor user account.
 */
public record GetAuditLogsByActorUserAccountIdQuery(Long actorUserAccountId) {
}