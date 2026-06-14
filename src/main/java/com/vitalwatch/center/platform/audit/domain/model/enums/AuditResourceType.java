package com.vitalwatch.center.platform.audit.domain.model.enums;

/**
 * Type of system resource affected by an auditable action.
 */
public enum AuditResourceType {
    PROFILE,
    USER_ACCOUNT,
    USER_INVITATION,
    HOSPITAL_WORKSPACE,
    ROLE_ASSIGNMENT,
    SUBSCRIPTION_PLAN,
    HOSPITAL_SUBSCRIPTION,
    VITAL_SIGN_READING,
    CLINICAL_RISK_ASSESSMENT,
    INCIDENT,
    WORK_SHIFT,
    SHIFT_ASSIGNMENT,
    RECOVERY_PLAN,
    RECOVERY_ACTION
}