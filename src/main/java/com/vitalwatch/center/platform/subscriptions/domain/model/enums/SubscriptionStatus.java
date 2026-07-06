package com.vitalwatch.center.platform.subscriptions.domain.model.enums;

/**
 * Represents the lifecycle status of an organization subscription.
 */
public enum SubscriptionStatus {
    ACTIVE,
    PENDING,
    CANCELLED,
    EXPIRED,
    PAST_DUE
}