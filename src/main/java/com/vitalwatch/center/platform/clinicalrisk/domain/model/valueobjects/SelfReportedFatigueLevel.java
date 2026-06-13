package com.vitalwatch.center.platform.clinicalrisk.domain.model.valueobjects;

/**
 * Self-reported fatigue level from 1 to 5.
 */
public record SelfReportedFatigueLevel(Integer value) {

    public SelfReportedFatigueLevel {
        if (value == null) {
            throw new IllegalArgumentException("Self-reported fatigue level must not be null");
        }
        if (value < 1 || value > 5) {
            throw new IllegalArgumentException("Self-reported fatigue level must be between 1 and 5");
        }
    }
}