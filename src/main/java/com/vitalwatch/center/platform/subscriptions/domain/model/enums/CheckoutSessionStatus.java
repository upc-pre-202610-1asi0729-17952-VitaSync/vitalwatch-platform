package com.vitalwatch.center.platform.subscriptions.domain.model.enums;

/**
 * Represents the lifecycle status of a checkout session.
 */
public enum CheckoutSessionStatus {
    PENDING,
    COMPLETED,
    CANCELLED,
    EXPIRED
}