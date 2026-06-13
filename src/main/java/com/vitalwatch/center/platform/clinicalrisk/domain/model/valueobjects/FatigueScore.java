package com.vitalwatch.center.platform.clinicalrisk.domain.model.valueobjects;

/**
 * Fatigue score from 0 to 100.
 */
public record FatigueScore(Integer value) {

    public FatigueScore {
        if (value == null) {
            throw new IllegalArgumentException("Fatigue score must not be null");
        }
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("Fatigue score must be between 0 and 100");
        }
    }
}