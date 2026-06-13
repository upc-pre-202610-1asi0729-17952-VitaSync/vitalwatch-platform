package com.vitalwatch.center.platform.subscriptions.domain.model.valueobjects;

/**
 * Functional limits included in a VitalWatch subscription plan.
 */
public record PlanLimits(
        Integer maxDoctors,
        Integer maxSupervisors,
        Integer maxTeams,
        Integer maxWorkAreas,
        Integer monthlyInvitations,
        Integer dataHistoryDays
) {
    public PlanLimits {
        validateNonNegative(maxDoctors, "maxDoctors");
        validateNonNegative(maxSupervisors, "maxSupervisors");
        validateNonNegative(maxTeams, "maxTeams");
        validateNonNegative(maxWorkAreas, "maxWorkAreas");
        validateNonNegative(monthlyInvitations, "monthlyInvitations");
        validateNonNegative(dataHistoryDays, "dataHistoryDays");
    }

    private static void validateNonNegative(Integer value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " must not be null");
        }

        if (value < 0) {
            throw new IllegalArgumentException(fieldName + " must not be negative");
        }
    }
}