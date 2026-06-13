package com.vitalwatch.center.platform.incidents.domain.model.enums;

/**
 * Lifecycle status for an incident.
 */
public enum IncidentStatus {
    OPEN,
    ACKNOWLEDGED,
    ESCALATED,
    RESOLVED,
    CANCELLED
}