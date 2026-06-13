package com.vitalwatch.center.platform.clinicalrisk.domain.model.valueobjects;

/**
 * Heart rate in beats per minute.
 */
public record HeartRateBpm(Integer value) {

    public HeartRateBpm {
        if (value == null) {
            throw new IllegalArgumentException("Heart rate must not be null");
        }
        if (value < 30 || value > 240) {
            throw new IllegalArgumentException("Heart rate must be between 30 and 240 bpm");
        }
    }
}