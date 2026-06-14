package com.vitalwatch.center.platform.audit.domain.model.enums;

/**
 * Type of action recorded in the audit trail.
 */
public enum AuditActionType {
    CREATE,
    UPDATE,
    DELETE,
    LOGIN,
    LOGOUT,
    ROLE_ASSIGNMENT,
    INVITATION_SENT,
    INCIDENT_CREATED,
    INCIDENT_ESCALATED,
    INCIDENT_RESOLVED,
    RECOVERY_PLAN_CREATED,
    RECOVERY_PLAN_COMPLETED,
    SHIFT_ASSIGNED,
    SHIFT_COMPLETED
}