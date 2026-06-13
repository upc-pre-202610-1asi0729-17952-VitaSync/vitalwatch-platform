package com.vitalwatch.center.platform.subscriptions.domain.model.aggregates;

import com.vitalwatch.center.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.vitalwatch.center.platform.subscriptions.domain.model.commands.CreateSubscriptionPlanCommand;
import com.vitalwatch.center.platform.subscriptions.domain.model.enums.BillingPeriod;
import com.vitalwatch.center.platform.subscriptions.domain.model.enums.SubscriptionPlanCode;
import com.vitalwatch.center.platform.subscriptions.domain.model.enums.SubscriptionPlanStatus;
import com.vitalwatch.center.platform.subscriptions.domain.model.enums.SupportLevel;
import com.vitalwatch.center.platform.subscriptions.domain.model.valueobjects.Money;
import com.vitalwatch.center.platform.subscriptions.domain.model.valueobjects.PlanLimits;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Aggregate root that represents a commercial VitalWatch subscription plan.
 */
public class SubscriptionPlan extends AbstractDomainAggregateRoot<SubscriptionPlan> {

    private Long id;
    private SubscriptionPlanCode code;
    private String name;
    private String description;
    private Money price;
    private BillingPeriod billingPeriod;
    private PlanLimits limits;
    private SupportLevel supportLevel;
    private SubscriptionPlanStatus status;

    public SubscriptionPlan(
            Long id,
            SubscriptionPlanCode code,
            String name,
            String description,
            Money price,
            BillingPeriod billingPeriod,
            PlanLimits limits,
            SupportLevel supportLevel,
            SubscriptionPlanStatus status
    ) {
        this.id = id;
        this.code = Objects.requireNonNull(code, "code must not be null");
        this.name = validateName(name);
        this.description = validateDescription(description);
        this.price = Objects.requireNonNull(price, "price must not be null");
        this.billingPeriod = Objects.requireNonNull(billingPeriod, "billingPeriod must not be null");
        this.limits = Objects.requireNonNull(limits, "limits must not be null");
        this.supportLevel = Objects.requireNonNull(supportLevel, "supportLevel must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
    }

    public SubscriptionPlan(CreateSubscriptionPlanCommand command) {
        this(
                null,
                command.code(),
                command.name(),
                command.description(),
                new Money(command.priceAmount(), command.currency()),
                command.billingPeriod(),
                new PlanLimits(
                        command.maxDoctors(),
                        command.maxSupervisors(),
                        command.maxTeams(),
                        command.maxWorkAreas(),
                        command.monthlyInvitations(),
                        command.dataHistoryDays()
                ),
                command.supportLevel(),
                SubscriptionPlanStatus.ACTIVE
        );
    }

    public void activate() {
        this.status = SubscriptionPlanStatus.ACTIVE;
    }

    public void deprecate() {
        this.status = SubscriptionPlanStatus.DEPRECATED;
    }

    public boolean isActive() {
        return this.status == SubscriptionPlanStatus.ACTIVE;
    }

    private String validateName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Subscription plan name must not be null or blank");
        }
        return value.trim();
    }

    private String validateDescription(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Subscription plan description must not be null or blank");
        }
        return value.trim();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubscriptionPlanCode getCode() {
        return code;
    }

    public String getCodeAsString() {
        return code.name();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Money getPrice() {
        return price;
    }

    public BigDecimal getPriceAmount() {
        return price.amount();
    }

    public String getCurrency() {
        return price.currency();
    }

    public BillingPeriod getBillingPeriod() {
        return billingPeriod;
    }

    public PlanLimits getLimits() {
        return limits;
    }

    public Integer getMaxDoctors() {
        return limits.maxDoctors();
    }

    public Integer getMaxSupervisors() {
        return limits.maxSupervisors();
    }

    public Integer getMaxTeams() {
        return limits.maxTeams();
    }

    public Integer getMaxWorkAreas() {
        return limits.maxWorkAreas();
    }

    public Integer getMonthlyInvitations() {
        return limits.monthlyInvitations();
    }

    public Integer getDataHistoryDays() {
        return limits.dataHistoryDays();
    }

    public SupportLevel getSupportLevel() {
        return supportLevel;
    }

    public SubscriptionPlanStatus getStatus() {
        return status;
    }
}