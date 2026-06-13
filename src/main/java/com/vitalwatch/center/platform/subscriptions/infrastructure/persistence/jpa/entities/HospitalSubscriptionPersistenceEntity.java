package com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.entities;

import com.vitalwatch.center.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.vitalwatch.center.platform.subscriptions.domain.model.enums.SubscriptionStatus;
import jakarta.persistence.*;

import java.time.Instant;

/**
 * JPA persistence entity for hospital subscriptions.
 */
@Entity
@Table(name = "hospital_subscriptions")
public class HospitalSubscriptionPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "hospital_workspace_id", nullable = false)
    private Long hospitalWorkspaceId;

    @Column(name = "subscription_plan_id", nullable = false)
    private Long subscriptionPlanId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

    public HospitalSubscriptionPersistenceEntity() {
    }

    public Long getHospitalWorkspaceId() {
        return hospitalWorkspaceId;
    }

    public void setHospitalWorkspaceId(Long hospitalWorkspaceId) {
        this.hospitalWorkspaceId = hospitalWorkspaceId;
    }

    public Long getSubscriptionPlanId() {
        return subscriptionPlanId;
    }

    public void setSubscriptionPlanId(Long subscriptionPlanId) {
        this.subscriptionPlanId = subscriptionPlanId;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(Instant cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }
}