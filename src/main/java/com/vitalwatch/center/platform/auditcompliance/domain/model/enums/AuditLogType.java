package com.vitalwatch.center.platform.auditcompliance.domain.model.enums;

/**
 * Represents the type of auditable system action.
 */
public enum AuditLogType {
    USER_INVITED,
    USER_REGISTERED,
    USER_ROLE_CHANGED,
    USER_STATUS_CHANGED,
    TEAM_CREATED,
    TEAM_UPDATED,
    TEAM_STATUS_CHANGED,
    ALERT_RESOLVED,
    ANOMALY_REVIEWED,
    ANOMALY_DISMISSED,
    PREVENTIVE_ACTION_CREATED,
    PREVENTIVE_ACTION_COMPLETED,
    SHIFT_CHECK_IN,
    SHIFT_CHECK_OUT
}