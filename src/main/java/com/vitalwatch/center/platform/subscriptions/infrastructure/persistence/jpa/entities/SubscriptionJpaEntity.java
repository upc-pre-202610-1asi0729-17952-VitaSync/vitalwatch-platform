package com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.entities;

import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities.OrganizationJpaEntity;
import com.vitalwatch.center.platform.subscriptions.domain.model.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * JPA entity for organization subscriptions.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "subscriptions")
public class SubscriptionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private OrganizationJpaEntity organization;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_id", nullable = false)
    private PlanJpaEntity plan;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private SubscriptionStatus status = SubscriptionStatus.ACTIVE;

    @Column(name = "started_at", nullable = false)
    private LocalDate startedAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDate expiresAt;

    @Column(name = "stripe_customer_id", length = 120)
    private String stripeCustomerId;

    @Column(name = "stripe_subscription_id", length = 120)
    private String stripeSubscriptionId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public SubscriptionJpaEntity(
            OrganizationJpaEntity organization,
            PlanJpaEntity plan,
            LocalDate startedAt,
            LocalDate expiresAt
    ) {
        this.organization = organization;
        this.plan = plan;
        this.startedAt = startedAt;
        this.expiresAt = expiresAt;
        this.status = SubscriptionStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        this.status = SubscriptionStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsPastDue() {
        this.status = SubscriptionStatus.PAST_DUE;
        this.updatedAt = LocalDateTime.now();
    }

    public void expire() {
        this.status = SubscriptionStatus.EXPIRED;
        this.updatedAt = LocalDateTime.now();
    }
}