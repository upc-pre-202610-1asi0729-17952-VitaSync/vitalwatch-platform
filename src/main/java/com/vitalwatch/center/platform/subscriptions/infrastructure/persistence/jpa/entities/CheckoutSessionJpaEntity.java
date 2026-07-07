package com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.entities;

import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities.OrganizationJpaEntity;
import com.vitalwatch.center.platform.subscriptions.domain.model.enums.CheckoutSessionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * JPA entity for checkout sessions.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "checkout_sessions",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_checkout_sessions_session_id", columnNames = "session_id")
        }
)
public class CheckoutSessionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false, length = 120)
    private String sessionId;

    @Column(name = "checkout_url", nullable = false, length = 2048)
    private String checkoutUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private OrganizationJpaEntity organization;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_id", nullable = false)
    private PlanJpaEntity plan;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private CheckoutSessionStatus status = CheckoutSessionStatus.COMPLETED;

    @Column(name = "success_url", nullable = false, length = 500)
    private String successUrl;

    @Column(name = "cancel_url", nullable = false, length = 500)
    private String cancelUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public CheckoutSessionJpaEntity(
            String sessionId,
            String checkoutUrl,
            OrganizationJpaEntity organization,
            PlanJpaEntity plan,
            String successUrl,
            String cancelUrl
    ) {
        this.sessionId = sessionId;
        this.checkoutUrl = checkoutUrl;
        this.organization = organization;
        this.plan = plan;
        this.successUrl = successUrl;
        this.cancelUrl = cancelUrl;
        this.status = CheckoutSessionStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        this.status = CheckoutSessionStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    public void expire() {
        this.status = CheckoutSessionStatus.EXPIRED;
        this.updatedAt = LocalDateTime.now();
    }

    public void complete() {
        this.status = CheckoutSessionStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }
}