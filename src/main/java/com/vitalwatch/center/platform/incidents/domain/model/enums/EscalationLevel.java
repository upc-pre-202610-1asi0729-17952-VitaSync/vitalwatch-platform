package com.vitalwatch.center.platform.incidents.domain.model.enums;

/**
 * Escalation level used when an incident requires attention from higher roles.
 */
public enum EscalationLevel {
    NONE,
    CLINICAL_SUPERVISOR,
    MEDICAL_DIRECTOR,
    HOSPITAL_ADMIN
}