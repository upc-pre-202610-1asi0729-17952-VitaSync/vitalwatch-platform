package com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA entity for subscription plans.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "plans",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_plans_code", columnNames = "code")
        }
)
public class PlanJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, length = 40)
    private String code;

    @Column(name = "name", nullable = false, length = 80)
    private String name;

    @Column(name = "description", nullable = false, length = 250)
    private String description;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "currency", nullable = false, length = 10)
    private String currency = "USD";

    @Column(name = "billing_period", nullable = false, length = 30)
    private String billingPeriod = "MONTHLY";

    @Column(name = "max_doctors", nullable = false)
    private Integer maxDoctors;

    @Column(name = "max_supervisors", nullable = false)
    private Integer maxSupervisors;

    @Column(name = "max_teams", nullable = false)
    private Integer maxTeams;

    @Column(name = "max_work_areas", nullable = false)
    private Integer maxWorkAreas;

    @Column(name = "monthly_invitations", nullable = false)
    private Integer monthlyInvitations;

    @Column(name = "data_history_days", nullable = false)
    private Integer dataHistoryDays;

    @Column(name = "support_level", nullable = false, length = 80)
    private String supportLevel;

    @Column(name = "recommended", nullable = false)
    private Boolean recommended = false;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "plan_feature_keys",
            joinColumns = @JoinColumn(name = "plan_id")
    )
    @Column(name = "feature_key", nullable = false, length = 120)
    private List<String> featureKeys = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "plan_disabled_module_keys",
            joinColumns = @JoinColumn(name = "plan_id")
    )
    @Column(name = "disabled_module_key", nullable = false, length = 120)
    private List<String> disabledModuleKeys = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public PlanJpaEntity(
            String code,
            String name,
            String description,
            BigDecimal price,
            String currency,
            String billingPeriod,
            Integer maxDoctors,
            Integer maxSupervisors,
            Integer maxTeams,
            Integer maxWorkAreas,
            Integer monthlyInvitations,
            Integer dataHistoryDays,
            String supportLevel,
            Boolean recommended,
            Boolean enabled,
            List<String> featureKeys,
            List<String> disabledModuleKeys
    ) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.billingPeriod = billingPeriod;
        this.maxDoctors = maxDoctors;
        this.maxSupervisors = maxSupervisors;
        this.maxTeams = maxTeams;
        this.maxWorkAreas = maxWorkAreas;
        this.monthlyInvitations = monthlyInvitations;
        this.dataHistoryDays = dataHistoryDays;
        this.supportLevel = supportLevel;
        this.recommended = recommended;
        this.enabled = enabled;
        this.featureKeys = featureKeys;
        this.disabledModuleKeys = disabledModuleKeys;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}