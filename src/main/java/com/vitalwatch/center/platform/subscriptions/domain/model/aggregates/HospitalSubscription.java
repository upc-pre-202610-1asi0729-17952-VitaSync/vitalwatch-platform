package com.vitalwatch.center.platform.subscriptions.domain.model.aggregates;

import com.vitalwatch.center.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.vitalwatch.center.platform.subscriptions.domain.model.commands.SubscribeHospitalWorkspaceCommand;
import com.vitalwatch.center.platform.subscriptions.domain.model.enums.SubscriptionStatus;

import java.time.Instant;
import java.util.Objects;

/**
 * Aggregate root that represents a hospital workspace subscription.
 */
public class HospitalSubscription extends AbstractDomainAggregateRoot<HospitalSubscription> {

    private Long id;
    private Long hospitalWorkspaceId;
    private Long subscriptionPlanId;
    private SubscriptionStatus status;
    private Instant startedAt;
    private Instant cancelledAt;
    private Instant expiresAt;

    public HospitalSubscription(
            Long id,
            Long hospitalWorkspaceId,
            Long subscriptionPlanId,
            SubscriptionStatus status,
            Instant startedAt,
            Instant cancelledAt,
            Instant expiresAt
    ) {
        this.id = id;
        this.hospitalWorkspaceId = validatePositiveId(hospitalWorkspaceId, "hospitalWorkspaceId");
        this.subscriptionPlanId = validatePositiveId(subscriptionPlanId, "subscriptionPlanId");
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.startedAt = Objects.requireNonNull(startedAt, "startedAt must not be null");
        this.cancelledAt = cancelledAt;
        this.expiresAt = expiresAt;
    }

    public HospitalSubscription(SubscribeHospitalWorkspaceCommand command) {
        this(
                null,
                command.hospitalWorkspaceId(),
                command.subscriptionPlanId(),
                SubscriptionStatus.ACTIVE,
                Instant.now(),
                null,
                null
        );
    }

    public void changePlan(Long newSubscriptionPlanId) {
        if (!isActive()) {
            throw new IllegalStateException("Only active subscriptions can change plan");
        }
        this.subscriptionPlanId = validatePositiveId(newSubscriptionPlanId, "newSubscriptionPlanId");
    }

    public void cancel() {
        if (this.status == SubscriptionStatus.CANCELLED) {
            throw new IllegalStateException("Subscription is already cancelled");
        }
        this.status = SubscriptionStatus.CANCELLED;
        this.cancelledAt = Instant.now();
    }

    public void expire() {
        this.status = SubscriptionStatus.EXPIRED;
        this.expiresAt = Instant.now();
    }

    public boolean isActive() {
        return this.status == SubscriptionStatus.ACTIVE;
    }

    private Long validatePositiveId(Long value, String fieldName) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be a positive number");
        }
        return value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHospitalWorkspaceId() {
        return hospitalWorkspaceId;
    }

    public Long getSubscriptionPlanId() {
        return subscriptionPlanId;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getCancelledAt() {
        return cancelledAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}